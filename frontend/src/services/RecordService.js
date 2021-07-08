import axios from "axios";
import authHeader from "./AuthHeader";


const API_URL = "http://localhost:8080/home/";
let message;

    const addRecord = (newPhrase) =>{
        //const [message, setMessage] = useState("");
        return axios
            .post(API_URL, {
                newPhrase
            }, {headers: authHeader()})
            .then(response => {
                message = response.data.message;
                //console.log(message)
                //response.data.message
                //window.location.reload();
            });
    };

    const getMessage = () => {
        return message;
    }


    const removeRecord = (recordId) =>{
    return axios
        .delete(API_URL + recordId, {headers: authHeader()})
        .then(response => {
            window.location.reload();
        });
    };

export default {addRecord, removeRecord, getMessage};