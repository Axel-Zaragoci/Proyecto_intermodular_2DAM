import express from 'express';
import connectDB from './config/db.js';
import dotenv from "dotenv"
import cors from "cors";
console.clear();
dotenv.config()
import bookingRouter from './booking/bookingRouter.js';
import roomsRouter from './rooms/roomsRouter.js';
import morgan from 'morgan';
import usersRouter from './users/usersRouter.js';


const PORT = 3000;
const app = express();

connectDB()

app.use(cors());
app.use(express.json());
app.use(morgan("dev"))
app.use((req, res, next) => {
  console.log(`[${new Date().toISOString()}] ${req.method} ${req.originalUrl}`);
  next();
});

app.use('/booking', bookingRouter);
app.use("/room", roomsRouter);
app.use("/user", usersRouter);

app.listen(PORT, () => {
    console.log(`Servidor en el puerto ${PORT}`);
})