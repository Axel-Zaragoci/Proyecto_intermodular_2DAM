import { reviewDatabaseModel } from "./reviewsModel.js";

/**
 * Crea una nueva reseña
 * POST /review
 */
export async function createReview(req, res) {
    try {
        const { user, room, booking, rating, description } = req.body;

        if (!user || !room || !booking || !rating || !description) {
            return res.status(400).json({ message: "Todos los campos son requeridos" });
        }

        const review = new reviewDatabaseModel({
            user,
            room,
            booking,
            rating,
            description,
        });

        const saved = await review.save();
        return res.status(201).json(saved);
    } catch (err) {
        console.error("Error al crear reseña:", err.message);
        return res.status(500).json({ message: "Error interno del servidor" });
    }
}

/**
 * Obtiene todas las reseñas
 * GET /review
 */
export async function getReviews(req, res) {
    try {
        const reviews = await reviewDatabaseModel.find()
            .populate("user", "name email")
            .populate("room", "roomNumber type")
            .populate("booking", "checkInDate checkOutDate");
        return res.status(200).json(reviews);
    } catch (err) {
        console.error("Error al obtener reseñas:", err.message);
        return res.status(500).json({ message: "Error interno del servidor" });
    }
}

/**
 * Obtiene una reseña por ID
 * GET /review/:reviewID
 */
export async function getReviewById(req, res) {
    try {
        const { reviewID } = req.params;
        const review = await reviewDatabaseModel.findById(reviewID)
            .populate("user", "name email")
            .populate("room", "roomNumber type")
            .populate("booking", "checkInDate checkOutDate");

        if (!review) {
            return res.status(404).json({ message: "Reseña no encontrada" });
        }

        return res.status(200).json(review);
    } catch (err) {
        console.error("Error al obtener reseña:", err.message);
        return res.status(500).json({ message: "Error interno del servidor" });
    }
}

/**
 * Obtiene reseñas por habitación
 * GET /review/room/:roomID
 */
export async function getReviewsByRoom(req, res) {
    try {
        const { roomID } = req.params;
        const reviews = await reviewDatabaseModel.find({ room: roomID })
            .populate("user", "name email")
            .populate("booking", "checkInDate checkOutDate");
        return res.status(200).json(reviews);
    } catch (err) {
        console.error("Error al obtener reseñas por habitación:", err.message);
        return res.status(500).json({ message: "Error interno del servidor" });
    }
}

/**
 * Obtiene reseñas por usuario
 * GET /review/user/:userID
 */
export async function getReviewsByUser(req, res) {
    try {
        const { userID } = req.params;
        const reviews = await reviewDatabaseModel.find({ user: userID })
            .populate("room", "roomNumber type")
            .populate("booking", "checkInDate checkOutDate");
        return res.status(200).json(reviews);
    } catch (err) {
        console.error("Error al obtener reseñas por usuario:", err.message);
        return res.status(500).json({ message: "Error interno del servidor" });
    }
}

/**
 * Elimina una reseña
 * DELETE /review/:reviewID
 */
export async function deleteReview(req, res) {
    try {
        const { reviewID } = req.params;
        const deleted = await reviewDatabaseModel.findByIdAndDelete(reviewID);

        if (!deleted) {
            return res.status(404).json({ message: "Reseña no encontrada" });
        }

        return res.status(200).json({ message: "Reseña eliminada correctamente" });
    } catch (err) {
        console.error("Error al eliminar reseña:", err.message);
        return res.status(500).json({ message: "Error interno del servidor" });
    }
}
