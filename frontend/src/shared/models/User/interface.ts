export enum Role {
    USER = "USER",
    ADMIN = "ADMIN",
    LESSOR = "LESSOR",
    LESSEE = "LESSEE"
}

export interface IUser {
    username: string;
    password: string;
    role: Role;
}
