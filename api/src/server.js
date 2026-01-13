import express from 'express';
require("dotenv").config();
//import bookingRouter from './booking/bookingRouter.js';
//import roomsRouter from './rooms/roomsController.js';
//import usersRouter from './users/usersController.js';

const PORT = process.env.PORT;
const app = express();

app.use(express.json());

//app.use('/booking', bookingRouter);
//app.use("/room", roomsRouter);
//app.use("/user", usersRouter);

app.listen(PORT, () => {
    console.log(`Servidor en el puerto ${PORT}`);
})