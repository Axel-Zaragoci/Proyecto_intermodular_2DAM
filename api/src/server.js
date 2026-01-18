import express from 'express';
import connectDB from './config/db.js';
import dotenv from "dotenv"
import morgan from 'morgan';

dotenv.config()
import bookingRouter from './booking/bookingRouter.js';
import roomsRouter from './rooms/roomsRouter.js';
//import usersRouter from './users/usersRouter.js';
import { connectEmail, sendEmail } from './lib/mail/mailing.js';

const PORT = process.env.APP_PORT;
const app = express();

connectDB()
connectEmail();

app.use(express.json());
app.use(morgan("dev"))

app.use('/booking', bookingRouter);
app.use("/room", roomsRouter);
//app.use("/user", usersRouter);

app.listen(PORT, () => {
    console.log(`Servidor en el puerto ${PORT}`);
})