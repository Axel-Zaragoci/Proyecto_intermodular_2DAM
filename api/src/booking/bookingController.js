import { bookingDatabaseModel, BookingEntryData } from "./bookingModel";

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