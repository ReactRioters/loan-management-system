import { createLoan } from '../../services/loanService';
import { useAuth } from '../../hooks/useAuth';
import { useNavigate } from 'react-router-dom';
import { useFormik } from 'formik';

const ApplyLoan = () => {
    const { user } = useAuth();
    const navigate = useNavigate();

    const formik = useFormik({
        initialValues: {
            loanType: '',
            amount: '',
            tenure: ''
        },
        validate: (values) => {
            const errors = {};
            const amount = Number(values.amount);
            const tenure = Number(values.tenure);

            if (!['PERSONAL', 'HOME', 'AUTO'].includes(values.loanType)) {
                errors.loanType = 'Select a loan type.';
            }
            if (!values.amount || !Number.isFinite(amount) || amount <= 0) {
                errors.amount = 'Enter an amount greater than zero.';
            }
            if (!values.tenure || !Number.isInteger(tenure) || tenure <= 0) {
                errors.tenure = 'Enter a whole number of years greater than zero.';
            }

            return errors;
        },
        onSubmit: async (values) => {
            const loanData = {
                loanType: values.loanType,
                amount: Number(values.amount),
                tenure: Number(values.tenure),
                customer: {
                    id: user.customerId
                }
            };
            try {
                await createLoan(loanData);
                navigate('/customer/dashboard');
            } catch (error) {
                const status = error.response?.status;
                formik.setStatus(
                    status === 401
                        ? 'Your session has expired. Please sign in again.'
                        : status === 403
                            ? 'You do not have permission to apply for a loan.'
                            : status === 400
                                ? 'Please review your details and try again.'
                                : 'We could not submit your application. Please try again.'
                );
            }
        }
    });

    const fieldClass = (hasError) => `mt-2 w-full rounded-md border bg-white px-3 py-3 text-slate-900 outline-none transition focus:ring-2 ${
        hasError
            ? 'border-rose-400 focus:border-rose-500 focus:ring-rose-100'
            : 'border-slate-300 focus:border-teal-600 focus:ring-teal-100'
    }`;

    return (
        <main className="min-h-screen bg-slate-100 px-4 py-10 sm:px-6">
            <section className="mx-auto max-w-2xl overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm">
                <header className="border-b border-slate-200 bg-slate-50 px-6 py-7 sm:px-9">
                    <p className="text-sm font-semibold uppercase tracking-wider text-teal-700">Loan application</p>
                    <h1 className="mt-2 text-2xl font-semibold text-slate-900">Tell us what you need</h1>
                    <p className="mt-2 text-sm text-slate-600">Choose a loan type and share the amount and repayment period.</p>
                </header>

                <form className="space-y-6 px-6 py-7 sm:px-9" onSubmit={formik.handleSubmit} noValidate>
                    {formik.status && (
                        <div className="rounded-md border border-rose-200 bg-rose-50 px-4 py-3 text-sm text-rose-800" role="alert">
                            {formik.status}
                        </div>
                    )}

                    <div>
                        <label htmlFor="loanType" className="block text-sm font-medium text-slate-800">Loan type</label>
                        <select
                            id="loanType"
                            name="loanType"
                            value={formik.values.loanType}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            aria-invalid={Boolean(formik.touched.loanType && formik.errors.loanType)}
                            aria-describedby={formik.touched.loanType && formik.errors.loanType ? 'loanType-error' : undefined}
                            className={fieldClass(Boolean(formik.touched.loanType && formik.errors.loanType))}
                        >
                            <option value="">Choose a loan type</option>
                            <option value="PERSONAL">Personal</option>
                            <option value="HOME">Home</option>
                            <option value="AUTO">Auto</option>
                        </select>
                        {formik.touched.loanType && formik.errors.loanType && (
                            <p id="loanType-error" className="mt-2 text-sm text-rose-700">{formik.errors.loanType}</p>
                        )}
                    </div>

                    <div>
                        <label htmlFor="amount" className="block text-sm font-medium text-slate-800">Loan amount</label>
                        <div className="relative">
                            <span className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-500" aria-hidden="true">₹</span>
                            <input
                                type="number"
                                id="amount"
                                name="amount"
                                min="0.01"
                                step="0.01"
                                inputMode="decimal"
                                placeholder="Enter amount"
                                value={formik.values.amount}
                                onChange={formik.handleChange}
                                onBlur={formik.handleBlur}
                                aria-invalid={Boolean(formik.touched.amount && formik.errors.amount)}
                                aria-describedby={formik.touched.amount && formik.errors.amount ? 'amount-error' : 'amount-hint'}
                                className={`${fieldClass(Boolean(formik.touched.amount && formik.errors.amount))} pl-8`}
                            />
                        </div>
                        {formik.touched.amount && formik.errors.amount ? (
                            <p id="amount-error" className="mt-2 text-sm text-rose-700">{formik.errors.amount}</p>
                        ) : (
                            <p id="amount-hint" className="mt-2 text-sm text-slate-500">Enter a positive amount in rupees.</p>
                        )}
                    </div>

                    <div>
                        <label htmlFor="tenure" className="block text-sm font-medium text-slate-800">Repayment period</label>
                        <div className="relative">
                            <input
                                type="number"
                                id="tenure"
                                name="tenure"
                                min="1"
                                step="1"
                                inputMode="numeric"
                                placeholder="Enter number of years"
                                value={formik.values.tenure}
                                onChange={formik.handleChange}
                                onBlur={formik.handleBlur}
                                aria-invalid={Boolean(formik.touched.tenure && formik.errors.tenure)}
                                aria-describedby={formik.touched.tenure && formik.errors.tenure ? 'tenure-error' : 'tenure-hint'}
                                className={`${fieldClass(Boolean(formik.touched.tenure && formik.errors.tenure))} pr-16`}
                            />
                            <span className="pointer-events-none absolute right-3 top-1/2 -translate-y-1/2 text-sm text-slate-500">years</span>
                        </div>
                        {formik.touched.tenure && formik.errors.tenure ? (
                            <p id="tenure-error" className="mt-2 text-sm text-rose-700">{formik.errors.tenure}</p>
                        ) : (
                            <p id="tenure-hint" className="mt-2 text-sm text-slate-500">Use a whole number of years.</p>
                        )}
                    </div>

                    <div className="border-t border-slate-200 pt-5">
                        <button
                            type="submit"
                            disabled={formik.isSubmitting}
                            className="w-full rounded-md bg-teal-700 px-5 py-3 font-semibold text-white transition hover:bg-teal-800 focus:outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-60 sm:w-auto"
                        >
                            {formik.isSubmitting ? 'Submitting application…' : 'Submit application'}
                        </button>
                    </div>
                </form>
            </section>
        </main>
    )
}

export default ApplyLoan;