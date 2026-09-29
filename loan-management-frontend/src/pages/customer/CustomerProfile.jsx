import { useEffect, useState } from "react";
import { getCustomerProfile } from "../../services/customerService";
import { useAuth } from "../../hooks/useAuth";

const CustomerProfile = () => {
    const [profile, setProfile] = useState(null);
    const { user } = useAuth();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const customerId = user?.customerId;
                const profileData = await getCustomerProfile(customerId);
                setProfile(profileData);
            } catch (error) {
                console.error("Error fetching customer profile:", error);
                setError("Failed to fetch customer profile.");
            } finally {
                setLoading(false);
            }
        };

        fetchProfile();
    }, []);

    return (
        <div className="p-6">
            <h1 className="text-3xl font-bold text-slate-800">
                Customer Profile
            </h1>
            <div className="mt-6">
                <h2 className="text-xl font-semibold text-slate-700">
                    Personal Information
                </h2>
                <div className="mt-4">
                    {loading ? (
                        <p>Loading...</p>
                    ) : error ? (
                        <p className="text-red-500">{error}</p>
                    ) : profile ? (
                        <div className="space-y-2">
                            <p><strong>Name:</strong> {profile.name}</p>
                            <p><strong>Email:</strong> {profile.email}</p>
                            <p><strong>Phone:</strong> {profile.phone}</p>
                            <p><strong>Address:</strong> {profile.address}</p>
                        </div>
                    ) : (
                        <p>No profile information available.</p>
                    )}
                </div>
            </div>
        </div>
    )
};

export default CustomerProfile;
