import { useState, useEffect } from 'react'
import axios from 'axios'
import './App.css'

function App() {
  const [simulServer,setSimulServer] = useState("ValorInicial")

  const [field1,setField1] = useState("")

  useEffect(() => {
console.log("=== old simulServer: "+simulServer)    
setField1(simulServer)
/*
    axios.get('http://localhost:8080/value')
      .then(response => {
        setSimulServer(response.data);
console.log("=== simulServer: "+simulServer)    
        setField1(response.data)
      })
      .catch(error => {
        console.error("=== useEffect axios error: "+error);
      });
*/      
  }, []);

  const handle_Input1_Change = value => {setField1(value)}

  //=== WORKING!!! DONT TOUCH!!!
  const handle_button1_click = (e) => {
            console.log('=== Clicked button_1: '+field1)

            axios.post('http://localhost:8080/postvalue',field1)
            .then(response => {
              setSimulServer(response.data);
              setField1(response.data)
            })
            .catch(error => {
              console.error("=== button axios error: "+error);
            });
  }

  return (
    <div className='root'>
      <div className='frame'>
        <input type='text' className='input1_style' value={field1} onChange={e => handle_Input1_Change(e.target.value)}/>
        <br/>
        <br/>
        <button className='button1_style' onClick={(e) => handle_button1_click(e)}>ATUALIZAR</button>
      </div>
    </div>
  )
}

export default App
