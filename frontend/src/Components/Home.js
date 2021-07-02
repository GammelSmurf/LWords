import React, {Component, useEffect, useState} from "react";
import UserService from "../services/UserService";

const Home = () => {
        const [content, setContent] = useState([{},{}]);

        useEffect(() => {
            UserService.getUserRecords().then(
                (response) => {
                    setContent(response.data);
                },
                (error) => {
                    const _content =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();

                    setContent(_content);
                }
            );
        }, []);


        return(
            <div className="container">
                {content.map(record =>
                    <div key={record.id}>
                        {record.phrase}
                    </div>
                )}
            </div>
        )
}
export default Home;