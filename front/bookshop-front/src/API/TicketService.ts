import axiosInstance from "../interceptor/axiosInterceptor";

interface TicketDetails {
    title: string
    message: string;
}

interface MessageDetails {
    message: string;
    uuid: string;
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

    static getUserTickets = async () => {
        return await axiosInstance.get("/ticket/all", {withCredentials: true});
    };

    static sendMessage = async (messageDetails: MessageDetails) => {
        return await axiosInstance.post("/ticket/messages/send", messageDetails, {withCredentials: true});
    };

    static closeTicket = async (uuid: string) => {
        return await axiosInstance.post("/ticket/close", {uuid: uuid}, {withCredentials: true});
    }
}