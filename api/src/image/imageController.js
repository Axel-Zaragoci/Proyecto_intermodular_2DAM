
export const uploadPhoto = (req, res) => {
  if (!req.file) {
    return res.status(400).json({ error: "Falta el archivo 'photo'." });
  }

  res.status(201).json({
    id: req.file.filename,
    originalName: req.file.originalname,
    mimetype: req.file.mimetype,
    size: req.file.size,
    url: `/uploads/${req.file.filename}`,
  });
};

const MAX_TOTAL = 20 * 1024 * 1024; // 20MB total

export const uploadPhotos = (req, res) => {
  const total = (req.files ?? []).reduce((acc, f) => acc + f.size, 0);
  if (total > MAX_TOTAL) {
    return res.status(413).json({ error: "Tamaño total excedido." });
  }
  const files = req.files.map(f => ({
    id: f.filename,
    originalName: f.originalname,
    mimetype: f.mimetype,
    size: f.size,
    url: `/uploads/${f.filename}`,
  }));

  res.status(201).json({ files });
};

