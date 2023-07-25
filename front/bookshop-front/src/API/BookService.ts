import axiosInstance from "../interceptor/axiosInterceptor"

interface BookData {
    title: string;
    genre: string;
    author: string;
}

export default class BookService {
    static getBooks = async (page: number, size: number, sortField: string, sortDirection: string) => {
        console.log(sortField,sortDirection)
        return await axiosInstance.get(`${process.env.REACT_APP_API_URL}/books/list`, {
            params: {
                page: page,
                size: size,
                sortField: sortField,
                sortDirection: sortDirection
            }, withCredentials: true
        });
    };

    static uploadBook = async (formData: FormData) => {
        return await axiosInstance.post(`${process.env.REACT_APP_API_URL}/books/upload`, formData, {withCredentials: true});
    }

    static updateBook = async (bookData: BookData, uuid: string) => {
        return await axiosInstance.post(`${process.env.REACT_APP_API_URL}/books/${uuid}/update`, bookData, {withCredentials: true});
    }

    static deleteBook = async (uuid: string) => {
        return await axiosInstance.delete(`${process.env.REACT_APP_API_URL}/books/${uuid}`, {withCredentials: true});
    }
}