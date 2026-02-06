import express from 'express';
import dotenv from "dotenv";
import cors from 'cors';
import morgan from 'morgan';

import { connectEmail } from './lib/mail/mailing.js';
import connectDB from './config/db.js';

import bookingRouter from "./booking/bookingRouter.js";
import roomsRouter from "./rooms/roomsRouter.js";
import usersRouter from "./users/usersRouter.js";
import authRouter from "./auth/authRouter.js";
import photoRouter from "./lib/image/imageRouter.js";
dotenv.config();
connectDB()
connectEmail();

const PORT = process.env.APP_PORT;
const app = express();

app.use(cors());
app.use(express.json());
app.use(morgan("dev"));

app.use("/uploads", express.static("uploads"));

app.use("/booking", bookingRouter);
app.use("/room", roomsRouter);
app.use("/user", usersRouter);
app.use("/auth", authRouter);
app.use("/image", photoRouter);

app.listen(PORT, () => {
  console.log(`Servidor en el puerto ${PORT}`);
});
