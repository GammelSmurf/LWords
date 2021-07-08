import React, {useEffect, useState} from "react";
import UserService from "../services/UserService";
import RecordTable from "./RecordTable";

const Home = () => {
        const [content, setContent] = useState([]);
        //const [modalActive, setModalActive] = useState(false);

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

        const data = []
        let arrTranslations = []
        let mainTranslation = ''
        let moreTranslations = ''
        let parsedDate = ''
        content.forEach(item => {
            arrTranslations = item.translations.split(';')
            mainTranslation = arrTranslations[0];
            arrTranslations.splice(0,1)
            moreTranslations = arrTranslations.join(', ').slice(0,-2)
            parsedDate = item.date.split('T').join(' ').slice(0, -7)
            data.push({id:item.id, phrase: item.phrase, mainTranslation: mainTranslation, progress: item.progress,
                moreTranslations: moreTranslations, date: parsedDate
            })
            }
        )
        return(
            <div className="container">
                <RecordTable data={data}/>
            </div>
        )
}
export default Home;