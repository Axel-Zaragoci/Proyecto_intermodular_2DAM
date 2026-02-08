import { Schema, model } from "mongoose";

/**
 * @typedef reviewSchema
 *
 * @property {import('mongoose').Types.ObjectId} _id - ID único de la reseña
 * @property {import('mongoose').Types.ObjectId} user - ID del usuario que hizo la reseña
 * @property {import('mongoose').Types.ObjectId} room - ID de la habitación reseñada
 * @property {import('mongoose').Types.ObjectId} booking - ID de la reserva asociada
 * @property {number} rating - Valoración (1-5)
 * @property {string} description - Descripción de la reseña
 */
const reviewDatabaseSchema = new Schema({
    user: {
        type: Schema.Types.ObjectId,
        ref: "user",
        required: true
    },
    room: {
        type: Schema.Types.ObjectId,
        ref: "room",
        required: true
    },
    booking: {
        type: Schema.Types.ObjectId,
        ref: "booking",
        required: true
    },
    rating: {
        type: Number,
        required: true,
        min: 1,
        max: 5
    },
    description: {
        type: String,
        required: true,
        trim: true
    },
}, { timestamps: true });

export const reviewDatabaseModel = model("review", reviewDatabaseSchema);
