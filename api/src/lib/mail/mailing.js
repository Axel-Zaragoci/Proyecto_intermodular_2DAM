import nodemailer from 'nodemailer'
import fs from 'fs/promises'
import path from 'path'
import { fileURLToPath } from 'url'
import Mustache from 'mustache'

const emailTemplates = new Map();

let emailTemplatesNames = [];

let transporter;

function getTemplatesDir() {
    const file = fileURLToPath(import.meta.url);
    const dir = path.dirname(file);
    return path.join(dir, 'templates');
}

async function getTemplateContent(template) {
    if (emailTemplates.has(template)) return emailTemplates.get(template);
    const file = path.join(getTemplatesDir(), `${template}.html`);
    const content = await fs.readFile(file, 'utf-8');
    emailTemplates.set(template, content);
    return content;
}

async function renderTemplate(template, data) {
    let templateContent = await getTemplateContent(template);
    console.log(data)
    return Mustache.render(templateContent, data);
}

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