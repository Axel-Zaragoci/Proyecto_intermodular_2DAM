import mongoose from 'mongoose';
const { Schema } = mongoose;

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