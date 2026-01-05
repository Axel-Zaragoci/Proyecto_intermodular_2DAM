import mongoose from 'mongoose';
const { Schema } = mongoose;

const schema = new Schema({
    room: ObjectId,
    client: ObjectId,
    checkInDate: Date,
    checkOutDate: Date,
    cancelDate: Date,
    price: Int32
});

export default mongoose.model('booking', schema)