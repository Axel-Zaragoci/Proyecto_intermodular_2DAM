import { Router } from "express";
import {
  createRoom,
  getRoomById,
  updateRoom,
  deleteRoom,
  getRoomsFiltered
} from "./roomsController.js";
import { authorizeRoles, verifyToken } from "../auth/authMiddleware.js";

const router = Router();

router.post("/", verifyToken, authorizeRoles(["Admin", "Trabajador"]), createRoom);
router.get("/", verifyToken, getRoomsFiltered);
router.get("/:roomID", verifyToken,getRoomById);
router.patch("/:roomID", verifyToken, authorizeRoles(["Admin", "Trabajador"]), updateRoom);
router.delete("/:roomID", verifyToken, authorizeRoles(["Admin", "Trabajador"]), deleteRoom);

export default router;
