import express from 'express';
import connectDB from './config/db.js';
import dotenv from "dotenv"

dotenv.config()
import bookingRouter from './booking/bookingRouter.js';
import roomsRouter from './rooms/roomsRouter.js';
import morgan from 'morgan';
//import usersRouter from './users/usersRouter.js';

const PORT = 3000;
const app = express();

connectDB()

app.use(express.json());
app.use(morgan("dev"))

app.use('/booking', bookingRouter);
app.use("/room", roomsRouter);
//app.use("/user", usersRouter);
zzapp.listen(PORT, () => {
    console.log(`Servidor en el puerto ${PORT}`);
})