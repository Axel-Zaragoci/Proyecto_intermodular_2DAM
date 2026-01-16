import { Router } from "express";
import {
  createRoom,
  getRoomById,
  updateRoom,
  deleteRoom,
  getRoomsFiltered
} from "./roomsController.js";

const router = Router();

router.post("/", createRoom);
router.get("/", getRoomsFiltered);
router.get("/:roomID", getRoomById);
router.patch("/:roomID", updateRoom);
router.delete("/:roomID", deleteRoom);

export default router;
