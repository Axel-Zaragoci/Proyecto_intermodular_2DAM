import { roomDatabaseModel, RoomEntryData } from "./roomsModel.js";

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
    const { roomID } = req.params;

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

    // Busca la room actual para usar valores por defecto si no vienen en el body
    const room = await roomDatabaseModel.findById(roomID);
    if (!room) return res.status(404).json({ message: "Room no encontrada" });

    // Construye un objeto de entrada con fallback a valores actuales
    const data = new RoomEntryData(
      type ?? room.type,
      roomNumber ?? room.roomNumber,
      maxGuests ?? room.maxGuests,
      description ?? room.description,
      mainImage ?? room.mainImage,
      pricePerNight ?? room.pricePerNight
    );

    // Completa campos opcionales con recuerdo a valores actuales
    data.completeRoomData(
      extraBed ?? room.extraBed,
      crib ?? room.crib,
      offer ?? room.offer,
      extras ?? room.extras,
      extraImages ?? room.extraImages
    );

    // Valida el objeto antes de actualizar
    data.validate();

    // Actualiza en BD
    const updated = await roomDatabaseModel.findByIdAndUpdate(
      roomID,
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
    const { roomID } = req.params;

    const deleted = await roomDatabaseModel.findByIdAndDelete(roomID);

    if (!deleted) return res.status(404).json({ message: "Room no encontrada" });

    return res.json({ message: "Room eliminada", deleted });
  } catch (err) {
    return res.status(400).json({ message: err.message });
  }
};

/**
 * Obtiene el listado de rooms con filtros por query params.
 *
 * @function getRoomsFiltered
 * @param {import('express').Request} req - Petición HTTP.
 * @param {Object.<string, string>} [req.query] - Parámetros de filtro opcionales.
 * @param {string} [req.query.name] - Filtro por nombre de la room.
 * @param {string} [req.query.type] - Filtro por tipo de room.
 * @param {string} [req.query.page] - Número de página para paginación.
 * @param {string} [req.query.limit] - Límite de resultados por página.
 * @param {import('express').Response} res - Respuesta HTTP.
 * @param {import('express').NextFunction} next - Siguiente middleware.
 * @returns {Promise<void>} No devuelve nada directamente, responde vía `res`.
 */
export const getRoomsFiltered = async (req, res) => {
  try {
    const {
      type,
      isAvailable,
      minPrice,
      maxPrice,
      guests,
      hasExtraBed,
      hasCrib,
      hasOffer,
      extras,      
      sortBy,      
      sortOrder,   
      limit,
      page,
    } = req.query;

    const filter = {};

    
    if (type) filter.type = String(type);

    
    if (isAvailable !== undefined) {
      filter.isAvailable = String(isAvailable).toLowerCase() === "true";
    }

    
    if (minPrice !== undefined || maxPrice !== undefined) {
      filter.pricePerNight = {};
      if (minPrice !== undefined) filter.pricePerNight.$gte = Number(minPrice);
      if (maxPrice !== undefined) filter.pricePerNight.$lte = Number(maxPrice);
    }

    
    if (guests !== undefined) {
      filter.maxGuests = { $gte: Number(guests) };
    }

    
    if (extras) {
      const extrasArr = String(extras)
        .split(",")
    g    .map((e) => e.trim())
        .filter(Boolean);

      if (extrasArr.length) {
        filter.extras = { $all: extrasArr };
      }
    }

    // flags
    if (hasExtraBed !== undefined) {
      filter.extraBed = String(hasExtraBed).toLowerCase() === "true";
    }
    if (hasCrib !== undefined) {
      filter.crib = String(hasCrib).toLowerCase() === "true";
    }
    if (hasOffer !== undefined) {
      const wantOffer = String(hasOffer).toLowerCase() === "true";
      filter.offer = wantOffer ? { $gt: 0 } : 0;
    }

    // sorting
    const allowedSort = new Set(["pricePerNight", "rate", "roomNumber", "type", "maxGuests"]);
    const sortField = allowedSort.has(sortBy) ? sortBy : "roomNumber";
    const sortDir = String(sortOrder).toLowerCase() === "desc" ? -1 : 1;
    const sort = { [sortField]: sortDir };

    // pagination
    const safeLimit = Math.min(Number(limit) || 20, 100);
    const safePage = Math.max(Number(page) || 1, 1);
    const skip = (safePage - 1) * safeLimit;

    const [items, total] = await Promise.all([
      roomDatabaseModel.find(filter).sort(sort).skip(skip).limit(safeLimit),
      roomDatabaseModel.countDocuments(filter),
    ]);

    return res.json({
      page: safePage,
      limit: safeLimit,
      total,
      totalPages: Math.ceil(total / safeLimit),
      items,
      appliedFilter: filter,
      sort,
    });
  } catch (err) {
    return res.status(400).json({ message: err.message });
  }
};
