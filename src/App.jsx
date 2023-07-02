import { useState, useEffect } from 'react'
import axios from 'axios'
import './App.css'

function App() {
  const [simulServer,setSimulServer] = useState("ValorInicial")

  const [field1,setField1] = useState("")

  const handle_Input1_Change = value => {setField1(value)}
  const handle_button1_click = (e) => {
            console.log('=== Clicked button_1: '+field1)

            axios.post('http://localhost:4000/updateFile')
            .then(response => {
              setSimulServer(response.data);
            })
            .catch(error => {
              console.error("=== button axios error: "+error);
            });
      



//            setSimulServer(field1)
          } //atualizar arquivo no servidor

  useEffect(() => {
    axios.get('http://localhost:4000/updateFile')
      .then(response => {
        setSimulServer(response.data);
      })
      .catch(error => {
        console.error("=== useEffect axios error: "+error);
      });


/*    
    console.log("=== Called useEffect: Ler dado do arquivo no servidor..."+simulServer)
    setField1(simulServer)

    const interval = setInterval(() => {
      console.log("== Temporizou...");
    }, 2000);
*/
    return //() => clearInterval(interval);
  }, []);


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
