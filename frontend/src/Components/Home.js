import React, {useState, useRef, useEffect} from 'react';
import BootstrapTable from 'react-bootstrap-table-next';
import ToolkitProvider,{Search} from 'react-bootstrap-table2-toolkit';
import { Modal, Button, Form, ProgressBar, Dropdown, Spinner } from 'react-bootstrap'
import RecordService from "../services/RecordService";
import AuthService from "../services/AuthService";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {faArrowUp, faArrowDown, faStar} from '@fortawesome/free-solid-svg-icons'
import UserService from "../services/UserService";

const Home = () => {
    const [isModalAddActive, setIsModalAddActive] = useState(false);
    const [isModalDeleteActive, setIsModalDeleteActive] = useState(false);
    const [isModalCsvActive, setIsModalCsvActive] = useState(false);
    const [isModalImportResponseActive, setIsModalImportResponseActive] = useState(false);
    const [responseMessage, setResponseMessage] = useState('');
    const [responseTextVisibility, setResponseTextVisibility] = useState(false);
    const [isHandleAddActive, setIsHandleAddActive] = useState(true);
    const [selectedRowCount, setSelectedRowCount] = useState(0);
    const [newPhrase, setNewPhrase] = useState('');
    const [selectedFile, setSelectedFile] = useState();
    const [isImportLoading, setIsImportLoading] = useState(false);
    const [isCsvWarningMessageVisible, setIsCsvWarningMessageVisible] = useState(false);
    const [csvWarningMessage, setCsvWarningMessage] = useState('');
    const [currentSortName, setCurrentSortName] = useState('Progress down');

    const [sortField, setSortField] = useState('progress');
    const [sortOrder, setSortOrder] = useState('desc');
    const [completedWordsIds, setCompletedWordsIds] = useState([]);
    const [hiddenRowKeys, setHiddenRowKeys] = useState([]);
    const [data, setData] = useState([]);

    const {SearchBar} = Search;
    const currentUser = AuthService.getCurrentUser();
    const addRecordInput = useRef(null);
    const table = useRef(null);
    const showCompletedCheckBox = useRef(null);

    const parseMainTranslation = (translations) => {
        return translations.split(';')[0];
    }

    const parseMoreTranslation = (translations) => {
        return translations.split(';').slice(1,-1).join(', ');
    }

    const parseDate = (itemDateTime) => {
        const dateTime = new Date(itemDateTime);
        const options = { year: '2-digit', month: '2-digit', day: '2-digit',
            hour: '2-digit', minute: '2-digit', second: '2-digit'};
        const dateTimeFormat = new Intl.DateTimeFormat('ru-RU', options).format;
        return dateTimeFormat(dateTime);
    }

    useEffect(() => {
        UserService.getUserRecords().then(
            (response) => {
                let dataPrev = [];
                let completedWordsIdsPrev = [];
                response.data.forEach(item => {
                    item.finished && completedWordsIdsPrev.push(item.id);
                    dataPrev.push({id:item.id, phrase: item.phrase, mainTranslation: parseMainTranslation(item.translations), progress: item.progress,
                        moreTranslations: parseMoreTranslation(item.translations), date: parseDate(item.date), finished: item.finished, incorrectAnsCount: item.incorrectAnsCount
                    })
                })
                setCompletedWordsIds(completedWordsIdsPrev);
                setHiddenRowKeys(completedWordsIdsPrev);
                setData(dataPrev);
            }
        );
    }, []);


    const handleModalAddClose = () => {setIsModalAddActive(false); window.location.reload();};
    const handleModalDeleteClose = () => {setIsModalDeleteActive(false)};
    const handleModalCsvClose = () => {
        if(!isImportLoading){
            setIsModalCsvActive(false);
            setIsImportLoading(false);
            setIsCsvWarningMessageVisible(false);
        }
    };
    const handleModalImportSuccessClose = () => {setIsModalImportResponseActive(false); window.location.reload();}
    const handleShow = () => setIsModalAddActive(true);

    const progressFormatter = (cell, row) => {
        let labelContent;
        let now;
        const progressLength = Number(currentUser.progressLength);
        if(completedWordsIds.includes(row.id)){
            labelContent = 'completed';
            now = 100;
        }
        else{
            labelContent = `${cell}` + '/' + `${progressLength}`;
            now = cell / progressLength * 100;
        }
        return (
            <span>
                <ProgressBar style={{marginTop: '5px'}} now={now} label={labelContent} striped/>
            </span>
        );
    }

    const columns = [{
        dataField: 'id',
        text: 'RecordId',
        hidden: true,
        csvExport: false
    },{
        dataField: 'date',
        text: 'RecordDate',
        hidden: true,
        csvExport: false
    },{
        dataField: 'incorrectAnsCount',
        text: 'RecordIncorrectAnsCount',
        hidden: true,
        csvExport: false
    },{
        dataField: 'phrase',
        text: 'Word / Phrase',
        style: { width: "40%"}
    }, {
        dataField: 'mainTranslation',
        text: 'Translation',
        style: { width: "40%"},
        csvExport: false
    }, {
        dataField: 'progress',
        text: 'Progress',
        style: { width: "20%" },
        sort: true,
        formatter: progressFormatter,
        csvType: Number
    }];

    const selectRow = {
        mode: 'checkbox',
        clickToSelect: true,
        clickToExpand: true,
        style: { backgroundColor: '#ECECEC' }
    };

    const MyExportCSV = (props) => {
        const handleClick = () => {
            props.onExport();
            handleModalCsvClose();
        };
        return (
            <Button variant='info' onClick={handleClick} disabled={isImportLoading}>Export</Button>
        );
    };

        const expandRow = {
            onlyOneExpanding: true,
            renderer: row => (
                <div>
                    <div>
                        <h6>More translations</h6>
                        <p>{row.moreTranslations}</p>
                        <hr />
                    </div>
                    <div>
                        <div className="expandRowDate">
                            <p><b>Added:</b> {row.date}</p>
                        </div>
                        <div className="expandRowButtons">
                            <Button variant='info' style={{marginRight: '10px'}}
                                    onClick={() => handleExamples(row.phrase)}>Examples</Button>
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
            if(isHandleAddActive){
                setIsHandleAddActive(false);
                RecordService.addRecord(newPhrase.toLowerCase())
                    .then(()=>{
                        setResponseTextVisibility(false);
                        setResponseMessage(RecordService.getMessage());
                        addRecordInput.current.value = '';
                        setResponseTextVisibility(true);
                        setIsHandleAddActive(true);
                    })
            }
        }
        const handleDeleteButton = () => {
            const recordIds = table.current.selectionContext.selected;
            setSelectedRowCount(recordIds.length);
            setIsModalDeleteActive(true);
        }

        const handleRemove = () => {
            const recordIds = table.current.selectionContext.selected;
            RecordService.removeRecord(recordIds).then();
        }

        const handleCsv = () =>{
            setIsModalCsvActive(true);
        }

        const handleExamples = (phrase) => {
            window.open('https://context.reverso.net/' +
                '%D0%BF%D0%B5%D1%80%D0%B5%D0%B2%D0%BE%D0%B4/' +
                '%D0%B0%D0%BD%D0%B3%D0%BB%D0%B8%D0%B9%D1%81%D0%BA%D0%B8%D0%B9' +
                '-%D1%80%D1%83%D1%81%D1%81%D0%BA%D0%B8%D0%B9/' + phrase);
        }
        const AddByEnter = (e) => {
            if (e.keyCode === 13 && isHandleAddActive) {
                handleAdd();
            }
        }

        const CustomToggle = React.forwardRef(({ children, onClick }, ref) => (
            <a
                href=""
                ref={ref}
                onClick={(e) => {
                    e.preventDefault();
                    onClick(e);
                }}
                className='sortButton'
            >
                {children}
                &#x25bc;
            </a>
        ));

        const handleSortProgressUp = () => {
            setSortField('progress');
            setSortOrder('asc');
            setCurrentSortName('Progress up');
        }

        const handleSortProgressDown = () => {
            setSortField('progress');
            setSortOrder('desc');
            setCurrentSortName('Progress down');
        }

        const handleSortDateUp = () => {
            setSortField('date');
            setSortOrder('asc');
            setCurrentSortName('Date up');
        }

        const handleSortDateDown = () => {
            setSortField('date');
            setSortOrder('desc');
            setCurrentSortName('Date down');
        }

        const handleSortDifficulty = () => {
            setSortField('incorrectAnsCount');
            setSortOrder('desc');
            setCurrentSortName('Most difficult');
        }

        const handleShowCompleted = () => {
            showCompletedCheckBox.current.checked ? setHiddenRowKeys([])
                : setHiddenRowKeys(completedWordsIds);
        }

        const onFileChange = event => {
            event.target.files[0] ? setSelectedFile(event.target.files[0]) : setSelectedFile(null);
        };

        const onFileUpload = () => {
            if(selectedFile){
                if(selectedFile.type === 'application/vnd.ms-excel'){
                    const formData = new FormData();
                    formData.append(
                        "file",
                        selectedFile
                    );
                    setIsImportLoading(true);
                    RecordService.importCSV(formData).then(()=>{handleModalCsvClose();setResponseMessage(RecordService.getMessage());setIsModalImportResponseActive(true)});
                }
                else{
                    setIsCsvWarningMessageVisible(false);
                    setCsvWarningMessage('Wrong file type! .csv format is only available');
                    setTimeout(function(){ setIsCsvWarningMessageVisible(true); }, 0);
                }
            }
            else{
                setIsCsvWarningMessageVisible(false);
                setCsvWarningMessage('Please, choose file to import data');
                setTimeout(function(){ setIsCsvWarningMessageVisible(true); }, 0);
            }

        };

    return (
            <div className="container">
                <ToolkitProvider
                    keyField='id'
                    data={data}
                    columns={ columns }
                    search
                    exportCSV={ {
                        fileName: currentUser.username + '.csv',
                        separator: ';',
                        ignoreHeader: true,
                        noAutoBOM: false,
                        blobType: 'text/csv;charset=ansi',
                    } }
                >
                    {
                        props =>
                            <div style={{margin: '50px auto 0'}}>
                                <div className='toolkitGroup'>
                                    <div className='toolkitButtons'>
                                        <Button variant='primary' onClick={handleShow}>
                                            Add record
                                        </Button>
                                        <Button variant='secondary' onClick={handleDeleteButton}>
                                            Delete
                                        </Button>
                                        <Button variant='info' onClick={handleCsv}>CSV</Button>
                                    </div>
                                    <div style={{marginBottom: '5px'}}>
                                        <div className="searchBar">
                                            <SearchBar { ...props.searchProps } placeholder={'Search (' + (data.length-hiddenRowKeys.length).toString() + ')'}/>
                                        </div>
                                        <div className='sortGroup'>
                                            <Dropdown style={{display: 'inline-block'}}>
                                                <Dropdown.Toggle as={CustomToggle} id="dropdown-basic">
                                                    {currentSortName}
                                                </Dropdown.Toggle>
                                                <Dropdown.Menu>
                                                    <Dropdown.Item onClick={handleSortDifficulty}>Most difficult first <FontAwesomeIcon style={{marginBottom: '2px'}} icon={faStar}/></Dropdown.Item>
                                                    <Dropdown.Item onClick={handleSortProgressUp}>Progress up <FontAwesomeIcon icon={faArrowUp}/></Dropdown.Item>
                                                    <Dropdown.Item onClick={handleSortProgressDown}>Progress down <FontAwesomeIcon icon={faArrowDown}/></Dropdown.Item>
                                                    <Dropdown.Item onClick={handleSortDateUp}>Date up <FontAwesomeIcon icon={faArrowUp}/></Dropdown.Item>
                                                    <Dropdown.Item onClick={handleSortDateDown}>Date down <FontAwesomeIcon icon={faArrowDown}/></Dropdown.Item>
                                                </Dropdown.Menu>
                                            </Dropdown>
                                            <div className='form-check' style={{display: 'inline-block'}}>
                                                <input ref={showCompletedCheckBox} id='showCompleted' type='checkbox' className='form-check-input' onClick={handleShowCompleted}/>
                                                <label htmlFor='showCompleted' className='form-check-label'>Show completed</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <Modal onEntered={() => addRecordInput.current.focus()} show={isModalAddActive} onHide={handleModalAddClose}
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
                                                <Form.Control ref={addRecordInput} onChange={onChangePhrase} onKeyDown={e => AddByEnter(e)}/>
                                                <Form.Control type="text" style={{display: "none"}} />
                                                {responseTextVisibility &&
                                                <Form.Text className="text-danger responseText">
                                                    {responseMessage}
                                                </Form.Text>
                                                }
                                            </Form.Group>
                                        </Form>
                                    </Modal.Body>
                                    <Modal.Footer>
                                        <Button variant="secondary" onClick={handleModalAddClose}>
                                            Close
                                        </Button>
                                        <Button variant="primary" onClick={handleAdd}>
                                            Add
                                        </Button>
                                    </Modal.Footer>
                                </Modal>

                                <Modal show={isModalDeleteActive} onHide={handleModalDeleteClose}
                                       animation={true}>
                                    <Modal.Header>
                                        <Modal.Title>Confirmation</Modal.Title>
                                    </Modal.Header>
                                    <Modal.Body>
                                        <Form.Text>
                                            {selectedRowCount !== 0 ? 'Do you really want to delete ' + selectedRowCount + ' records?': 'Please, select which records you want to delete!'}
                                        </Form.Text>
                                    </Modal.Body>
                                    <Modal.Footer>
                                        <Button variant="secondary" onClick={handleModalDeleteClose}>
                                            {selectedRowCount !== 0 ? 'No' : 'Ok'}
                                        </Button>
                                        {selectedRowCount !== 0 &&
                                        <Button variant="primary" onClick={handleRemove}>
                                            Yes
                                        </Button>}
                                    </Modal.Footer>
                                </Modal>

                                <Modal show={isModalCsvActive} onHide={handleModalCsvClose}
                                       animation={true}>
                                    <Modal.Header>
                                        <Modal.Title>Choose</Modal.Title>
                                    </Modal.Header>
                                    <Modal.Body style={{height:'128px'}}>
                                        <Form.Text>
                                            To import data choose file (.csv) below or click 'export' to get (.csv) file of your table data
                                        </Form.Text>
                                        <input className='form-control' type='file' onChange={onFileChange} disabled={isImportLoading}/>
                                        {isCsvWarningMessageVisible &&
                                        <Form.Text className='text-danger responseText'>
                                            {csvWarningMessage}
                                        </Form.Text>}
                                    </Modal.Body>
                                    <Modal.Footer>
                                        <MyExportCSV { ...props.csvProps } />
                                        <Button variant='primary' onClick={onFileUpload} disabled={isImportLoading}>
                                            {isImportLoading && <Spinner
                                                as="span"
                                                animation="border"
                                                size="sm"
                                                role="status"
                                                aria-hidden="true"
                                                style={{marginRight:'8px'}}
                                            />}
                                            Import
                                        </Button>
                                    </Modal.Footer>
                                </Modal>

                                <Modal show={isModalImportResponseActive} onHide={handleModalImportSuccessClose}
                                       animation={true}>
                                    <Modal.Header>
                                        <Modal.Title>{Number(responseMessage) !== 0 ? 'Notification' : 'Error'}</Modal.Title>
                                    </Modal.Header>
                                    <Modal.Body>
                                        {Number(responseMessage) !== 0 ?
                                        <Form.Text>
                                            {responseMessage} records have been added successfully
                                        </Form.Text> :
                                        <Form.Text>
                                            Failed to add records... It could be because of several reasons:<br />
                                                1. Wrong format. Use 'phrase;progress' in each line.<br />
                                                2. File is empty.<br />
                                                3. Records are already exist in table.
                                        </Form.Text>}
                                    </Modal.Body>
                                    <Modal.Footer>
                                        <Button variant="primary" onClick={handleModalImportSuccessClose}>
                                            Ok
                                        </Button>
                                    </Modal.Footer>
                                </Modal>

                                <BootstrapTable ref={table} {...props.baseProps} hover expandRow={ expandRow } selectRow={ selectRow }
                                                sort={ {dataField: sortField, order: sortOrder} }
                                                hiddenRows={ hiddenRowKeys }
                                                noDataIndication={() => <p>Table is empty</p>}/>
                            </div>
                    }
                </ToolkitProvider>
            </div>
        );
}
export default Home;