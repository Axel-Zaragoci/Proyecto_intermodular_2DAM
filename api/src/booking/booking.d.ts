import type { Request } from 'express';

export interface AuthenticatedRequest extends Request {
    session: {
        userId: string
        role: 'admin' | 'employee' | 'customer'
    }
}