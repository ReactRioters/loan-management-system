const Unauthorized = () => {
    return (
        <div className="min-h-screen flex items-center justify-center bg-slate-100">
            <div className="w-full max-w-md rounded-xl bg-white p-8 shadow-lg">
                <h1 className="text-2xl font-bold text-slate-800">
                    Unauthorized
                </h1>
                <p className="mt-4 text-slate-600">
                    You are not authorized to access this page.
                </p>
            </div>
        </div>
    )
}

export default Unauthorized