import { bookingDatabaseModel, BookingEntryData } from "./bookingModel";
import { roomDatabaseModel } from "../rooms/roomsModel"
import mongoose from "mongoose";

/**
 * @type import("express").RequestHandler 
 * 
 * @response {200} - Devuelve la reserva encontrada
 * @response {400} - Falta el ID de la reserva
 * @response {404} - No se encontró la reserva
 * @response {500} - Error del servidor
 */
async function getOneBookingById(req, res) {
    try {
        const { bookingId } = req.body;
        if (!bookingId) return res.status(400).json({ error: 'Se requiere ID de la reserva' });

        const booking = await bookingDatabaseModel.findById(bookingId);
        if (!booking) return res.status(404).json({ error: 'Reserva no encontrada' });
        
        return res.status(200).json(booking);
    }
    catch (error) {
        console.error('Error al obtener la reserva:', error);
        return res.status(500).json({ error: 'Error del servidor' });
    }
}

/**
 * @type import("express").RequestHandler
 * 
 * @response {200} - Devuelve todas las reservas
 * @response {404} - No se encontraron reservas
 * @response {500} - Error del servidor 
 */
async function getBookings(req, res) {
    try {
        const bookings = await bookingDatabaseModel.find();
        if (!bookings) return res.status(404).json({ error: 'No se encontraron reservas' });
        
        return res.status(200).json(bookings);
    }
    catch (error) {
        console.error('Error al obtener la reserva:', error);
        return res.status(500).json({ error: 'Error del servidor' });
    }
}

/**
 * @type import("express").RequestHandler
 * 
 * @response {200} - Devuelve las reservas del cliente
 * @response {400} - Falta el ID del cliente
 * @response {404} - No se encontraron reservas para el cliente
 * @response {500} - Error del servidor
 */
async function getBookingsByClientId(req, res) {
    try {
        const { clientId } = req.body;
        if (!clientId) return res.status(400).json({ error: 'Se requiere ID del cliente' });

        const bookings = await bookingDatabaseModel.find({ client: clientId });
        if (!bookings) return res.status(404).json({ error: 'No se encontraron reservas para el cliente' });

        return res.status(200).json(bookings);
    }
    catch (error) {
        console.error('Error al obtener las reservas del cliente:', error);
        return res.status(500).json({ error: 'Error del servidor' });
    }
}

/**
 * @type import("express").RequestHandler
 *  
 * @response {200} - Devuelve las reservas de una habitación
 * @response {400} - ID de la habitación inexistente
 * @response {404} - Reservas no encontradas
 * @response {500} - Error del servidor
 */
async function getBookingsByRoomId(req, res) {
    try {
        const { roomID } = req.body;
        if (!roomID) return res.status(400).json({error: 'Se requiere ID de la habitación'});

        const bookings = await bookingDatabaseModel.find({ room: roomID});
        if (!bookings) return res.status(404).json({ error: 'No se ha encontrado reservas para la habitación' });

        return res.status(200).json(bookings);
    }
    catch (error) {
        console.error('Error al obtener las reservas de la habitación:', error);
        return res.status(500).json({ error: 'Error del servidor' });
    }
}


/**
 * @param {import("types").AuthenticatedRequest} req
 * @param {import("express").Response} res
 *  
 * @response {400} - Error de datos
 * @response {404} - Habitación no encontrada
 * @response {200} - Reserva creada correctamente
 */
async function createBooking(req, res) {
    try {
        const { roomID, checkInDate, checkOutDate, guests } = req.body;
        const userID = req.session.userId;
        if (!roomID) return res.status(400).json({ error: 'Se requiere ID de habitación'});
        if (!checkInDate) return res.status(400).json({ error: 'Se requiere fecha de check in' });
        if (!checkOutDate) return res.status(400).json({ error: 'Se requiere fecha de check out'});
        if (!guests) return res.status(400).json({ error: 'Se requiere cantidad de huéspedes' });

        const booking = new BookingEntryData(roomID, userID, checkInDate, checkOutDate, guests);
        const room = await roomDatabaseModel.findById(roomID);
        if (!room) return res.status(404).json({ error: 'No se encuentra habitación con ese ID' });
        booking.completeBookingData(room.pricePerNight, room.offer);
        try {
            booking.validate()
        }
        catch(err) {
            return res.status(400).json({ error: err.message });
        }
        bookingDatabaseModel.insertOne(booking);
        return res.status(200).json({ status: 'Reserva creada correctamente'})
    }
    catch (error) {
        console.error('Error al crear la reserva:', error);
        return res.status(500).json({ error: 'Error del servidor' });
    }
}

/**
 * 
 * @param {import("types").AuthenticatedRequest} req
 * @param {import("express").Response} res
 * 
 * @response {400} - Error de datos
 * @response {404} - Reserva no encontrada
 * @response {200} - Reserva actualizada
 * @response {500} - Error del servidor
 */
async function cancelBooking(req, res) {
    try {
        const { bookingID } = req.params;

        if (!mongoose.isValidObjectId(bookingID)) return res.status(400).json({ error: 'No es un ID' })
        const booking = await bookingDatabaseModel.findById(bookingID);
        if (!booking) return res.status(404).json({ error: 'No hay reserva con este ID' });
        if (booking.status != 'Abierta') return res.status(400).json({ error: 'La reserva no está abierta' })

        const bookingUpdated = await bookingDatabaseModel.updateOne({_id: bookingID}, {status: "Cancelada"});
        return res.status(200).json(bookingUpdated);
    }
    catch (error) {
        console.error('Error al cancelar la reserva: ', error);
        return res.status(500).json({ error: 'Error del servidor' });
    }
}