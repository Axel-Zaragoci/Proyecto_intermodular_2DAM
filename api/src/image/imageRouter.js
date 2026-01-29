import { Router } from "express";
import { upload } from "../services/imageService.js";
import { uploadPhoto,uploadPhotos } from "./imageController.js";

const router = Router();

// POST /image  (multipart/form-data con key "photo")
router.post("/", upload.single("photo"), uploadPhoto);
// POST /image/many  (multipart/form-data con key "photos")
router.post("/many", upload.array("photos", 20), uploadPhotos);


export default router;
