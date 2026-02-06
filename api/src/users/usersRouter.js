import { Router } from 'express';
import { getOneUserByIdOrDni, getAllUsers, getUsersByRol, registerUser, createUser } from './usersController.js';
import { verifyToken ,authorizeRoles } from '../auth/authMiddleware.js';    
const usersRouter = Router();

usersRouter.get('/', getAllUsers);
usersRouter.get('/rol/:rol', verifyToken, authorizeRoles(["Admin", "Trabajador"]), getUsersByRol);
usersRouter.get('/getOne', getOneUserByIdOrDni);

usersRouter.post('/register', registerUser);
usersRouter.post('/', verifyToken, authorizeRoles(["Admin", "Trabajador"]), createUser);

export default usersRouter;