import axiosInstance from "../interceptor/axiosInterceptor";

interface TicketDetails {
    title: string
    message: string;
}

export default class TicketService {
    static createTicket = async (ticketDetails: TicketDetails) => {
        return await axiosInstance.post("/ticket/create", ticketDetails, {withCredentials: true});
    };

    static getTickets = async () => {
        return await axiosInstance.get("/ticket/list", {withCredentials: true});
    };

    static getMessages = async (uuid: string) => {
        return await axiosInstance.post("/ticket/messages", {uuid: uuid}, {withCredentials: true});
    }
}