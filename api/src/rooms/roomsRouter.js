import { Router } from "express";
import {
  createRoom,
  getAllRooms,
  getRoomById,
  updateRoom,
  deleteRoom,
} from "./roomsController.js";

const router = Router();

router.post("/", createRoom);
router.get("/", getAllRooms);
router.get("/:roomID", getRoomById);
router.patch("/:roomID", updateRoom);
router.delete("/:roomID", deleteRoom);

export default router;
