import nodemailer from 'nodemailer'
import fs from 'fs/promises'
import path from 'path'
import { fileURLToPath } from 'url'
import Mustache from 'mustache'

/**
 * Cache de plantillas de correo (nombre -> contenido HTML).
 * @type {Map<string,string>}
 */
const emailTemplates = new Map();

/**
 * Nombres de las plantillas disponibles (sin extensión).
 * @type {string[]}
 */
let emailTemplatesNames = [];

/**
 * Transporter de nodemailer usado para enviar correos.
 * Se inicializa en `connectEmail()`.
 */
let transporter;

/**
 * Obtiene la ruta absoluta al directorio `templates` junto a este módulo.
 * @returns {string} Ruta al directorio de plantillas.
 */
function getTemplatesDir() {
    const file = fileURLToPath(import.meta.url);
    const dir = path.dirname(file);
    return path.join(dir, 'templates');
}

/**
 * Lee y cachea el contenido HTML de una plantilla.
 * Si la plantilla ya está en caché, la devuelve directamente.
 * @param {string} template - Nombre de la plantilla (sin extensión).
 * @returns {Promise<string>} Contenido HTML de la plantilla.
 */
async function getTemplateContent(template) {
    if (emailTemplates.has(template)) return emailTemplates.get(template);
    const file = path.join(getTemplatesDir(), `${template}.html`);
    const content = await fs.readFile(file, 'utf-8');
    emailTemplates.set(template, content);
    return content;
}

/**
 * Renderiza una plantilla Mustache con los datos proporcionados.
 * @param {string} template - Nombre de la plantilla (sin extensión).
 * @param {Object} data - Objeto con los datos a inyectar en la plantilla.
 * @returns {Promise<string>} HTML resultante tras el render.
 */
async function renderTemplate(template, data) {
    let templateContent = await getTemplateContent(template);
    return Mustache.render(templateContent, data);
}

/**
 * Envía un correo usando la plantilla indicada.
 * @param {string} to - Dirección(es) del/los destinatario(s). Puede ser una cadena o lista separada por comas.
 * @param {string} subject - Asunto del correo.
 * @param {string} template - Nombre de la plantilla a usar (sin extensión).
 * @param {Object} data - Datos para renderizar la plantilla.
 * @returns {Promise<import('nodemailer').SentMessageInfo>} Resultado de `transporter.sendMail()`.
 * @throws {Error} Si la plantilla no existe en `emailTemplatesNames`.
 */
export async function sendEmail(to, subject, template, data) {
    if (!emailTemplatesNames.includes(template)) throw new Error("La plantilla de correo no existe")
    
    const html = await renderTemplate(template, data);
    const mail = {
        from: `"Hotel Pere María" <${process.env.MAIL_USER}>`,
        to,
        subject,
        text: 'Por favor, utiliza un correo que soporte HTML para ver el mensaje',
        html,
    }
    
    return transporter.sendMail(mail)
}

/**
 * Conecta con el servicio SMTP y carga las plantillas disponibles en el
 * directorio `templates` adyacente a este módulo.
 * - Inicializa `transporter`.
 * - Verifica la conexión.
 * - Rellena `emailTemplatesNames` con los nombres de archivos `.html`.
 *
 * Esta función maneja errores localmente e imprime en consola si ocurre alguno.
 * @returns {Promise<void>}
 */
export async function connectEmail() {
    try {
        transporter = nodemailer.createTransport({
            // @ts-ignore
            host: process.env.MAIL_HOST,
            port: Number(process.env.MAIL_PORT),
            secure: false,
            pool: true,
            auth: {
                user: process.env.MAIL_USER,
                pass: process.env.MAIL_PASSWORD
            }
        })

        await transporter.verify();
        console.log("Conectado la servicio de correo")
    } catch (err) {
        console.error("Error al conectar con el servicio de correo:", err)
    }

    try {
        const dir = getTemplatesDir();
        const files = await fs.readdir(dir);
        emailTemplatesNames = files.filter(t => t.endsWith('.html')).map(t => t.replace(/\.html$/i, ''));
        console.log("Plantillas de correo cargadas");
    } catch (err) {
        console.error("Error al cargar las plantillas de correo:", err)
    }
}