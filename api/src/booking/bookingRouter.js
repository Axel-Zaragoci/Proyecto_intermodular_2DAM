import { Router } from "express";
import { getOneBookingById, 
    getBookings, 
    getBookingsByClientId, 
    getBookingsByRoomId,
    createBooking,
    cancelBooking,
    updateBooking,
    deleteBooking} from "./bookingController";

const router = Router();

router.get("/:id", getOneBookingById);
router.get("/client/:id", getBookingsByClientId);
router.get("/room/:id", getBookingsByRoomId);
router.get("/", getBookings);

router.post("/", createBooking);

router.patch("/:id/cancel", cancelBooking);
router.patch("/:id", updateBooking);

router.delete("/:id", deleteBooking);

export default router;