import { Router } from "express";
import { getOneBookingById, 
    getBookings, 
    getBookingsByClientId, 
    getBookingsByRoomId,
    createBooking,
    cancelBooking,
    updateBooking,
    deleteBooking} from "./bookingController.js";
import { verifyToken } from "../auth/authMiddleware.js";

const router = Router();

router.get("/:id", verifyToken, getOneBookingById);
router.get("/client/:id", verifyToken, getBookingsByClientId);
router.get("/room/:id", verifyToken, getBookingsByRoomId);
router.get("/", verifyToken, getBookings);

router.post("/", verifyToken, createBooking);

router.patch("/:id/cancel", verifyToken, cancelBooking);
router.patch("/:id", verifyToken, updateBooking);

router.delete("/:id", verifyToken, deleteBooking);

export default router;