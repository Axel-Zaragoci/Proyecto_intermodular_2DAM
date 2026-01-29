import express from 'express';
import connectDB from './config/db.js';
import dotenv from "dotenv"
import cors from "cors";
dotenv.config()
import bookingRouter from './booking/bookingRouter.js';
import roomsRouter from './rooms/roomsRouter.js';
import morgan from 'morgan';
import usersRouter from './users/usersRouter.js';
import authRouter from './auth/authRouter.js';


const PORT = 3000;
const app = express();

connectDB()

app.use(cors());
app.use(express.json());
app.use(morgan("dev"))

app.use('/booking', bookingRouter);
app.use("/room", roomsRouter);
app.use("/user", usersRouter);
app.use("/auth", authRouter);

app.listen(PORT, () => {
    console.log(`Servidor en el puerto ${PORT}`);
})