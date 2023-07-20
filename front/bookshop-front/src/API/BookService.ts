import axios from "axios";

export default class BookService {
    static getBooks = async () => {
        return await axios.get('http://localhost:8054/books/list', {withCredentials: true});
    };
}