import React, {Component, useState} from 'react';
import BootstrapTable from 'react-bootstrap-table-next';
import ToolkitProvider,{Search} from 'react-bootstrap-table2-toolkit';
import { Modal, Button, Form } from 'react-bootstrap'
import RecordService from "../services/RecordService";

const RecordTable = ({data}) => {
    const [modalActive, setModalActive] = useState(false);
    const [responseMessage, setResponseMessage] = useState('');
    const [responseTextVisibility, setResponseTextVisibility] = useState(false);
    const [isHandleAddActive, setIsHandleAddActive] = useState(true);
    const handleClose = () => {setModalActive(false); window.location.reload();};
    const handleShow = () => setModalActive(true);

    const [newPhrase, setNewPhrase] = useState('');
    const {SearchBar} = Search

        const columns = [{
            dataField: 'phrase',
            text: 'Phrase',
            style: { width: "40%" }
        }, {
            dataField: 'mainTranslation',
            text: 'Translation',
            style: { width: "40%" }
        }, {
            dataField: 'progress',
            text: 'Progress',
            style: { width: "20%" },
            sort: true
        }];

        const expandRow = {
            onlyOneExpanding: true,
            renderer: row => (
                <div>
                    <div>
                        <h6>More translations</h6>
                        <p>{row.moreTranslations}</p>
                        <hr style={{color: "#EB6864", height: "1px", opacity: 1}}/>
                    </div>
                    <div className="expandRowUnderLine">
                        <div className="expandRowDate">
                            <p><b>Added:</b> {row.date}</p>
                        </div>
                        <div className="expandRowButtons">
                            <button className="btn btn-info reversoButton" onClick={() => handleReverso(row.phrase)}>Reverso</button>
                            <button className="btn btn-danger" onClick={() => handleRemove(row.id)}>Delete</button>
                        </div>
                    </div>

                </div>
            )
        };

        const onChangePhrase = (e) => {
            const newPhrase = e.target.value;
            setNewPhrase(newPhrase);
        };

        const handleAdd = () => {
            setIsHandleAddActive(false);
            //setModalActive(false);
            RecordService.addRecord(newPhrase.toLowerCase())
                .then(()=>{
                    setResponseMessage(RecordService.getMessage());
                    Form.Control.current.value = ''
                    setResponseTextVisibility(false);
                    setResponseTextVisibility(true);
                    setIsHandleAddActive(true);
                })

        }
        const handleRemove = (id) => {
            RecordService.removeRecord(id);
        }
        const handleReverso = (phrase) => {
            window.open('https://context.reverso.net/' +
                '%D0%BF%D0%B5%D1%80%D0%B5%D0%B2%D0%BE%D0%B4/' +
                '%D0%B0%D0%BD%D0%B3%D0%BB%D0%B8%D0%B9%D1%81%D0%BA%D0%B8%D0%B9' +
                '-%D1%80%D1%83%D1%81%D1%81%D0%BA%D0%B8%D0%B9/' + phrase);
        }
        const AddByEnter = (e) => {
            if (e.keyCode === 13) {
                handleAdd();
            }
        }

        return (
            <ToolkitProvider
                keyField='phrase'
                data={data}
                columns={ columns }
                search
            >
                {
                    props =>
                        <div className={"recordTable"}>
                            <div className={"recordTableSearchBar"}>
                                <SearchBar { ...props.searchProps } />
                                <button className="btn btn-primary addRecord" onClick={handleShow}
                                >Add record</button>
                            </div>

                            <Modal onEntered={() => Form.Control.current.focus()} show={modalActive} onHide={handleClose}
                                   animation={true}>
                                <Modal.Header>
                                    <Modal.Title>New record</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Form>
                                        <Form.Group>
                                            <Form.Text className="text-muted">
                                                Type a single word or phrase
                                            </Form.Text>
                                            <Form.Control ref={Form.Control} onChange={onChangePhrase} onKeyDown={isHandleAddActive ? (e) => AddByEnter(e) : null}/>
                                            <Form.Control type="text" style={{display: "none"}} />
                                            {responseTextVisibility ?
                                                <Form.Text className="text-danger responseModalText">
                                                    {responseMessage}
                                                </Form.Text>
                                                : null
                                            }
                                        </Form.Group>
                                    </Form>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={handleClose}>
                                        Close
                                    </Button>
                                    <Button variant="primary" onClick={isHandleAddActive ? handleAdd : null}>
                                        Add
                                    </Button>
                                </Modal.Footer>
                            </Modal>

                            <BootstrapTable {...props.baseProps} hover expandRow={ expandRow }/>
                        </div>
                }
            </ToolkitProvider>
        );
}
export default RecordTable;