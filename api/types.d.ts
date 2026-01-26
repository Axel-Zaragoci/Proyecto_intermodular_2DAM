export * from './src/booking/booking.d.ts'
import "express";

declare global {
  namespace Express {
    interface User {
      id: string;
      rol: "Usuario" | "Trabajador" | "Admin";
    }

    interface Request {
      user?: User;
    }
  }
}

export {};