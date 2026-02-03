import express from "express";
import connectDB from "./config/db.js";
import path from "path";
import { fileURLToPath } from "url";
import dotenv from "dotenv";
import cors from "cors";
import morgan from "morgan";

import bookingRouter from "./booking/bookingRouter.js";
import roomsRouter from "./rooms/roomsRouter.js";
import usersRouter from "./users/usersRouter.js";
import authRouter from "./auth/authRouter.js";
import photoRouter from "./image/imageRouter.js";

console.clear();
dotenv.config();

const PORT = process.env.PORT || 3000;
const app = express();

connectDB();

app.use(cors());
app.use(express.json());
app.use(morgan("dev"));




const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const UPLOADS_DIR = path.join(__dirname, "../uploads");

app.use("/uploads", express.static(UPLOADS_DIR));

app.use("/booking", bookingRouter);
app.use("/room", roomsRouter);
app.use("/user", usersRouter);
app.use("/auth", authRouter);
app.use("/image", photoRouter);

app.listen(PORT, () => {
  console.log(`Servidor en el puerto ${PORT}`);
});
