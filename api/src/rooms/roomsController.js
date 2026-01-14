import { roomDatabaseModel, RoomEntryData } from "./roomsModel";

/**
 * Crea una nueva habitación.
 *
 * @async
 * @function createRoom
 * @param {import("express").Request} req - Request de Express (body con datos de la habitación).
 * @param {import("express").Response} res - Response de Express.
 * @returns {Promise<import("express").Response>} Respuesta HTTP con la habitación creada o un error.
 *
 * @example
 * // POST /rooms
 * // body: { type, roomNumber, maxGuests, description, mainImage, pricePerNight, ... }
 */
export const createRoom = async (req, res) => {
  try {
    const {
      type,
      roomNumber,
      maxGuests,
      description,
      mainImage,
      pricePerNight,
      extraBed,
      crib,
      offer,
      extras,
      extraImages,
    } = req.body;

    // Crea la entrada base con los campos obligatorios
    const entry = new RoomEntryData(
      type,
      roomNumber,
      maxGuests,
      description,
      mainImage,
      pricePerNight
    );

    // Completa campos opcionales con valores por defecto si no vienen en el body
    entry.completeRoomData(
      extraBed ?? false,
      crib ?? false,
      offer ?? 0,
      extras ?? [],
      extraImages ?? []
    );

    // Convierte a documento Mongoose y guarda
    const doc = entry.toDocument();
    const saved = await doc.save();

    return res.status(201).json(saved);
  } catch (err) {
    // Error de clave duplicada (por ejemplo roomNumber unique)
    if (err?.code === 11000) {
      return res.status(409).json({ message: "roomNumber ya existe" });
    }
    // Validación / datos incorrectos
    return res.status(400).json({ message: err.message });
  }
};

/**
 * Obtiene todas las habitaciones.
 *
 * @async
 * @function getAllRooms
 * @param {import("express").Request} req - Request de Express.
 * @param {import("express").Response} res - Response de Express.
 * @returns {Promise<import("express").Response>} Lista de habitaciones o error.
 *
 * @example
 * // GET /rooms
 */
export const getAllRooms = async (req, res) => {
  try {
    const rooms = await roomDatabaseModel.find();
    return res.json(rooms);
  } catch (err) {
    return res.status(500).json({ message: err.message });
  }
};

/**
 * Obtiene una habitación por su ID.
 *
 * @async
 * @function getRoomById
 * @param {import("express").Request} req - Request de Express (params.id).
 * @param {import("express").Response} res - Response de Express.
 * @returns {Promise<import("express").Response>} Habitación encontrada o error.
 *
 * @example
 * // GET /rooms/:id
 */
export const getRoomById = async (req, res) => {
  try {
    const { roomID } = req.params;

    const room = await roomDatabaseModel.findById(roomID);

    // Si no existe, 404
    if (!room) return res.status(404).json({ message: "no se encontro esa habitacion" });

    return res.json(room);
  } catch (err) {
    return res.status(500).json({ message: err.message });
  }
};

/**
 * Actualiza una habitación por su ID.
 *
 * Nota: este handler espera recibir `id` por params.
 * (En tu código faltaba `const { id } = req.params;` antes de usarlo.)
 *
 * @async
 * @function updateRoom
 * @param {import("express").Request} req - Request de Express (params.id y body con campos a actualizar).
 * @param {import("express").Response} res - Response de Express.
 * @returns {Promise<import("express").Response>} Habitación actualizada o error.
 *
 * @example
 * // PATCH /rooms/:id
 * // body: { description: "Nueva descripción" }
 */
export const updateRoom = async (req, res) => {
  try {
    const { id } = req.params;

    // Busca la room actual para usar valores por defecto si no vienen en el body
    const room = await roomDatabaseModel.findById(id);
    if (!room) return res.status(404).json({ message: "Room no encontrada" });

    // Construye un objeto de entrada con fallback a valores actuales
    const data = new RoomEntryData(
      req.body.type ?? room.type,
      req.body.roomNumber ?? room.roomNumber,
      req.body.maxGuests ?? room.maxGuests,
      req.body.description ?? room.description,
      req.body.mainImage ?? room.mainImage,
      req.body.pricePerNight ?? room.pricePerNight
    );

    // Completa campos opcionales con fallback a valores actuales
    data.completeRoomData(
      req.body.extraBed ?? room.extraBed,
      req.body.crib ?? room.crib,
      req.body.offer ?? room.offer,
      req.body.extras ?? room.extras,
      req.body.extraImages ?? room.extraImages
    );

    // Valida la entrada (si tu clase implementa validate)
    data.validate();

    // Actualiza en BD
    const updated = await roomDatabaseModel.findByIdAndUpdate(
      id,
      { $set: data },
      { new: true, runValidators: true }
    );

    if (!updated) return res.status(404).json({ message: "Room no encontrada" });

    return res.json(updated);
  } catch (err) {
    // Duplicado por unique (si roomNumber cambia a uno existente)
    if (err?.code === 11000) {
      return res.status(409).json({ message: "roomNumber ya existe" });
    }
    return res.status(400).json({ message: err.message });
  }
};

/**
 * Elimina una habitación por su ID.
 *
 * @async
 * @function deleteRoom
 * @param {import("express").Request} req - Request de Express (params.id).
 * @param {import("express").Response} res - Response de Express.
 * @returns {Promise<import("express").Response>} Mensaje de éxito con el doc eliminado o error.
 *
 * @example
 * // DELETE /rooms/:id
 */
export const deleteRoom = async (req, res) => {
  try {
    const { id } = req.params;

    const deleted = await roomDatabaseModel.findByIdAndDelete(id);

    if (!deleted) return res.status(404).json({ message: "Room no encontrada" });

    return res.json({ message: "Room eliminada", deleted });
  } catch (err) {
    return res.status(400).json({ message: err.message });
  }
};
