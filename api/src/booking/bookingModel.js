import mongoose from 'mongoose';
const { Schema } = mongoose;

/**
 * @typedef booking
 * @description Documento correspondiente a los datos en MongoDB
 * 
 * @property {string} _id - Identificador único creado por MongoDB
 * @property {string} room - Identificador de la habitación
 * @property {string} client - Identificador del cliente
 * 
 * @property {Date} checkInDate - Fecha de inicio de la reserva
 * @property {Date} checkOutDate - Fecha de fin de la reserva
 * @property {Date} payDate - Fecha de pago, como la reserva se paga al hacerse, se indica que el dato sea la fecha de creación de la reserva
 * 
 * @property {number} totalPrice - Precio total de la reserva
 * @property {number} pricePerNight - Precio por noche de la reserva (Se guarda para facilitar el mostrar información en las aplicaciones correspondientes)
 * @property {number} offer - Porcentaje de descuento (Se guarda para facilitar el mostrar información en las aplicaciones correspondientes)
 * 
 * @property {string} status - Estado de la reserva, "Finalizada" para las que ya han pasado el checkOutDate, "Cancelada" para las reservas canceladas por el usuario o empleados y "Abierta" para el resto
 * @property {number} guests - Cantidad de huéspedes en la habitación reservada
 */
const bookingDatabaseSchema = new Schema({
    room: {
        type: Schema.Types.ObjectId,
        required: true
    },
    client: {
        type: Schema.Types.ObjectId,
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
        default: Date.now()
    },
    totalPrice: {
        type: Schema.Types.Number,
        required: true
    },
    pricePerNight: {
        type: Schema.Types.Number,
        required: true
    },
    offer: {
        type: Schema.Types.Number,
        required: true
    },
    status: {
        type: String,
        enum: ["Finalizada", "Cancelada", "Abierta"],
        default: "Abierta"
    },
    guests: {
        type: Schema.Types.Number,
        required: true
    }
});

export const bookingDatabaseModel = mongoose.model('booking', bookingDatabaseSchema)

/**
 * @typedef bookingEntryData
 * @description Schema de los datos entrantes necesarios para crear una reserva
 * 
 * @property {string} room - Identificador de la habitación reservada
 * @property {string} client - Identificador del cliente que realiza la reserva
 * 
 * @property {Date} checkInDate - Fecha de inicio
 * @property {Date} checkOutDate - Fecha de fin
 * 
 * @property {number} guests - Cantidad de huéspedes en la habitación
 */
export const bookingEntryDataSchema = new Schema({
    room: {
        type: Schema.Types.ObjectId,
        required: true
    },
    client: {
        type: Schema.Types.ObjectId,
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
    guests: {
        type: Schema.Types.Number,
        required: true
    }
})