import { Schema, Types, model, isValidObjectId } from 'mongoose';
import { formatDate, parseDate } from '../commons/date.js';

/**
 * @typedef {Object} bookingSchema
 * 
 * @property {import('mongoose').Types.ObjectId} room - Identificador de la habitación
 * @property {import('mongoose').Types.ObjectId} client - Identificador del cliente
 * @property {Date} checkInDate - Fecha de inicio de la reserva
 * @property {Date} checkOutDate - Fecha de fin de la reserva
 * @property {Date} payDate - Fecha de pago, como la reserva se paga al hacerse, se indica que el dato sea la fecha de creación de la reserva
 * @property {number} totalPrice - Precio total de la reserva
 * @property {number} pricePerNight - Precio por noche de la reserva (Se guarda para facilitar el mostrar información en las aplicaciones correspondientes)
 * @property {number} offer - Porcentaje de descuento (Se guarda para facilitar el mostrar información en las aplicaciones correspondientes)
 * @property {"Finalizada"|"Cancelada"|"Abierta"} status - Estado de la reserva, "Finalizada" para las que ya han pasado el checkOutDate, "Cancelada" para las reservas canceladas por el usuario o empleados y "Abierta" para el resto
 * @property {number} guests - Cantidad de huéspedes en la habitación reservada
 * @property {number} totalNights - Cantidad total de noches
 * 
 * @description Documento correspondiente a los datos en MongoDB
*/
const bookingDatabaseSchema = new Schema({
    room: {
        type: Types.ObjectId,
        required: true
    },
    client: {
        type: Types.ObjectId,
        required: true
    },
    checkInDate: {
        type: Date,
        required: true
    },
    checkOutDate: {
        type: Date,
        required: true
    },
    payDate: {
        type: Date,
        default: Date.now
    },
    totalPrice: {
        type: Number,
        required: true
    },
    pricePerNight: {
        type: Number,
        required: true
    },
    offer: {
        type: Number,
        required: true
    },
    status: {
        type: String,
        enum: ["Finalizada", "Cancelada", "Abierta"],
        default: "Abierta"
    },
    guests: {
        type: Number,
        required: true
    },
    totalNights: {
        type: Number,
        required: true
    }
}, {
    toJSON: {
        /**
         * @param {{[x: string] : unknown}} _doc 
         * @param {Record<String, any>} ret 
         * @returns 
         */
        transform(_doc, ret) {
            ret.checkInDate = formatDate(ret.checkInDate);
            ret.checkOutDate = formatDate(ret.checkOutDate);
            ret.payDate = formatDate(ret.payDate);
            return ret;
        }
    }
});

export const bookingDatabaseModel = model('booking', bookingDatabaseSchema)

/** Clase que obtiene los datos para la reserva */
export class BookingEntryData {
    /**
     * Crea una nueva entrada de datos
     * @param {import('mongoose').Types.ObjectId | string} roomID 
     * @param {import('mongoose').Types.ObjectId | string} clientID 
     * @param {string|Date} checkInDate 
     * @param {string|Date} checkOutDate 
     * @param {number} guests 
     */
    constructor(roomID, clientID, checkInDate, checkOutDate, guests) {
        this.roomID = roomID
        this.clientID = clientID
        this.checkInDate = typeof(checkInDate) === 'string' ? parseDate(checkInDate) : checkInDate
        this.checkOutDate = typeof(checkOutDate) === 'string' ? parseDate(checkOutDate) : checkOutDate
        this.guests = guests
        this.ready = false
    }

    /**
     * Completa los datos de la reserva para poder crearla
     * @param {number} pricePerNight 
     * @param {number} offer 
     */
    completeBookingData(pricePerNight, offer) {
        this.offer = offer
        this.totalNights = Math.ceil((this.checkOutDate.getTime() - this.checkInDate.getTime()) / (1000 * 60 * 60 * 24))
        this.pricePerNight = pricePerNight * (1 - offer / 100)
        this.totalPrice = this.totalNights * this.pricePerNight
        this.ready = true
    }

    /**
     * Añadir un ID existente
     * @param {string} id 
     */
    setID(id) {
        this._id = id;
    }

    async validate() {
        function isNumeric(o) {
            return typeof(o) === 'number' && o > 0
        }
        function isDate(o) {
            return o != null
        }
        

        const errors = [];
        if (!isValidObjectId(this.roomID)) errors.push("El ID de la habitación es inválido");
        if (!isValidObjectId(this.clientID)) errors.push("El ID del usuario es inválido");

        if (!isDate(this.checkInDate)) errors.push("La fecha de check-in no es válida");
        if (!isDate(this.checkOutDate)) errors.push("La fehca de check-out es inválida");
        if (isDate(this.checkOutDate) && isDate(this.checkInDate) && this.checkOutDate.getTime() < this.checkInDate.getTime()) errors.push("La fecha de fin no puede ser anterior a la de inicio")
        const now = new Date(Date.now()).setHours(0, 0, 0, 0);
        if (new Date(now).getTime() > this.checkInDate.getTime()) errors.push("No se puede hacer una reserva en el pasado")
        if (await dateOverlap(this._id, this.roomID, this.checkInDate, this.checkOutDate)) errors.push("Ya hay reserva en esas fechas")
        
        if (!isNumeric(this.guests)) errors.push("La cantidad de huéspedes debe ser un número mayor que 0");

        if (errors.length != 0) {
            throw new Error(errors.join(", "));
        }
    }

    /**
     * 
     * @returns {Promise}
     */
    save() {
        if (!this.ready) throw new Error("Reserva no lista. Completa la información");
        const doc = this.doc ?? new bookingDatabaseModel();
        const data = {room: this.roomID, 
                        client: this.clientID, 
                        checkInDate: this.checkInDate, 
                        checkOutDate: this.checkOutDate, 
                        totalPrice: this.totalPrice, 
                        pricePerNight: this.pricePerNight, 
                        offer: this.offer, 
                        guests: this.guests, 
                        totalNights: this.totalNights};
        
        return doc.set(data).save();
    }

    async fromDocument(booking) {
        this.doc = booking;
    }
}

async function dateOverlap(id, roomID, checkInDate, checkOutDate) {
    const exists = await bookingDatabaseModel.exists({
        room: roomID,
        checkInDate: { $lt: checkOutDate },
        checkOutDate: { $gt: checkInDate }
    })
    return exists && exists._id != id
}