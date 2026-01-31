import fs from "fs/promises";
import path from "path";

const UPLOAD_DIR = path.join(process.cwd(), "uploads");

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

// DELETE /image/:filename
export const deletePhoto = async (req, res) => {
  try {
    const { filename } = req.params;

    // Seguridad: solo nombre de archivo (sin carpetas)
    const safeName = path.basename(filename);
    if (!safeName || safeName !== filename) {
      return res.status(400).json({ error: "Nombre de archivo no válido." });
    }

    const filePath = path.join(UPLOAD_DIR, safeName);

    // Comprueba que existe
    await fs.access(filePath);

    // Borra
    await fs.unlink(filePath);

    return res.json({ ok: true, deleted: `/uploads/${safeName}` });
  } catch (err) {
    // Si no existe
    if (err?.code === "ENOENT") {
      return res.status(404).json({ error: "Archivo no encontrado." });
    }
    return res.status(500).json({ error: "Error borrando el archivo." });
  }
};

// (Opcional) DELETE /image  { files: ["a.jpg","b.jpg"] }
export const deletePhotos = async (req, res) => {
  try {
    const files = Array.isArray(req.body?.files) ? req.body.files : [];
    if (files.length === 0) {
      return res.status(400).json({ error: "Body inválido. Usa { files: [] }" });
    }

    const results = [];

    for (const f of files) {
      const safeName = path.basename(f);
      if (!safeName || safeName !== f) {
        results.push({ file: f, ok: false, error: "nombre no válido" });
        continue;
      }

      const filePath = path.join(UPLOAD_DIR, safeName);

      try {
        await fs.access(filePath);
        await fs.unlink(filePath);
        results.push({ file: safeName, ok: true });
      } catch (e) {
        results.push({
          file: safeName,
          ok: false,
          error: e?.code === "ENOENT" ? "no existe" : "error borrando",
        });
      }
    }

    return res.json({ ok: true, results });
  } catch {
    return res.status(500).json({ error: "Error borrando archivos." });
  }
};
