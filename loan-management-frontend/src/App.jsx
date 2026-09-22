import { useState } from 'react'
import './App.css'

function App() {

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-100">
      <div className="rounded-xl bg-white p-8 shadow-lg text-center">
        <h1 className="text-3xl font-bold text-slate-800">
          Loan Management System
        </h1>

        <p className="mt-3 text-slate-600">
          React + JavaScript + Tailwind CSS
        </p>

        <button className="mt-6 rounded-lg bg-blue-600 px-5 py-2 text-white hover:bg-blue-700">
          Tailwind Working
        </button>
      </div>
    </div>
  )
}

export default App
