import axiosInstance from "../interceptor/axiosInterceptor"
import {CredentialsToUpdate} from "../types/Credentials";

export default class ProfileService {
    static getProfile = async () => {
        return await axiosInstance.get("/profile/me", {withCredentials: true});
    };

    static updateCredentials = async (credentials: CredentialsToUpdate) => {
        console.log(credentials)
        return await axiosInstance.post("/profile/update", credentials, {withCredentials: true});
    }
}