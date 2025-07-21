import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useCartStore, useAuthStore } from "../../store";
import { Input, Button } from "../../components/common";
import { orderService } from "../../services/order.service";
import type { AddressRequest } from "../../types";

export function CheckoutPage() {
    const navigate = useNavigate();
    const { items, getTotalPrice, clearCart } = useCartStore();
    const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
    const [isProcessing, setIsProcessing] = useState(false);
    const [error, setError] = useState("");

    const [shippingAddress, setShippingAddress] = useState<AddressRequest>({
        firstName: "",
        lastName: "",
        streetNumber: "",
        street: "",
        addressComplement: "",
        postalCode: "",
        city: "",
        country: "France",
    });

    const [useSameAddress, setUseSameAddress] = useState(true);
    const [billingAddress, setBillingAddress] = useState<AddressRequest>({
        ...shippingAddress,
    });

    // Redirect if not authenticated or cart is empty
    if (!isAuthenticated) {
        navigate("/login", { state: { from: { pathname: "/checkout" } } });
        return null;
    }

    if (items.length === 0) {
        navigate("/cart");
        return null;
    }

    const handleShippingChange = (
        field: keyof AddressRequest,
        value: string
    ) => {
        setShippingAddress((prev) => ({ ...prev, [field]: value }));
        if (useSameAddress) {
            setBillingAddress((prev) => ({ ...prev, [field]: value }));
        }
    };

    const handleBillingChange = (
        field: keyof AddressRequest,
        value: string
    ) => {
        setBillingAddress((prev) => ({ ...prev, [field]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        setIsProcessing(true);

        try {
            // Create order
            const order = await orderService.createOrder();

            // Update addresses
            await orderService.updateOrderAddresses(order.id, {
                shippingAddress,
                billingAddress: useSameAddress
                    ? shippingAddress
                    : billingAddress,
            });

            // Clear cart and redirect to payment
            clearCart();
            navigate(`/payment/${order.id}`); // Change this route
        } catch (err: any) {
            setError(err.response?.data?.reason || "Failed to create order");
        } finally {
            setIsProcessing(false);
        }
    };

    const totalPrice = getTotalPrice();

    return (
        <div className="max-w-4xl mx-auto">
            <h1 className="text-3xl font-bold mb-8 text-foreground">
                Checkout
            </h1>

            {error && (
                <div className="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-400 px-4 py-3 rounded mb-6">
                    {error}
                </div>
            )}

            <form onSubmit={handleSubmit}>
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* Address Forms */}
                    <div className="lg:col-span-2 space-y-8">
                        {/* Shipping Address */}
                        <div className="bg-card rounded-lg shadow p-6 border border-border">
                            <h2 className="text-xl font-bold mb-4 text-card-foreground">
                                Shipping Address
                            </h2>
                            <div className="grid grid-cols-2 gap-4">
                                <Input
                                    label="First Name"
                                    value={shippingAddress.firstName}
                                    onChange={(e) =>
                                        handleShippingChange(
                                            "firstName",
                                            e.target.value
                                        )
                                    }
                                    required
                                />
                                <Input
                                    label="Last Name"
                                    value={shippingAddress.lastName}
                                    onChange={(e) =>
                                        handleShippingChange(
                                            "lastName",
                                            e.target.value
                                        )
                                    }
                                    required
                                />
                                <Input
                                    label="Street Number"
                                    value={shippingAddress.streetNumber}
                                    onChange={(e) =>
                                        handleShippingChange(
                                            "streetNumber",
                                            e.target.value
                                        )
                                    }
                                    required
                                    className="col-span-1"
                                />
                                <Input
                                    label="Street"
                                    value={shippingAddress.street}
                                    onChange={(e) =>
                                        handleShippingChange(
                                            "street",
                                            e.target.value
                                        )
                                    }
                                    required
                                    className="col-span-1"
                                />
                                <Input
                                    label="Address Complement"
                                    value={
                                        shippingAddress.addressComplement || ""
                                    }
                                    onChange={(e) =>
                                        handleShippingChange(
                                            "addressComplement",
                                            e.target.value
                                        )
                                    }
                                    className="col-span-2"
                                    placeholder="Apartment, suite, etc. (optional)"
                                />
                                <Input
                                    label="Postal Code"
                                    value={shippingAddress.postalCode}
                                    onChange={(e) =>
                                        handleShippingChange(
                                            "postalCode",
                                            e.target.value
                                        )
                                    }
                                    required
                                    pattern="[0-9]{5}"
                                    placeholder="75001"
                                />
                                <Input
                                    label="City"
                                    value={shippingAddress.city}
                                    onChange={(e) =>
                                        handleShippingChange(
                                            "city",
                                            e.target.value
                                        )
                                    }
                                    required
                                />
                            </div>
                        </div>

                        {/* Billing Address */}
                        <div className="bg-card rounded-lg shadow p-6 border border-border">
                            <h2 className="text-xl font-bold mb-4 text-card-foreground">
                                Billing Address
                            </h2>
                            <label className="flex items-center mb-4 text-foreground">
                                <input
                                    type="checkbox"
                                    checked={useSameAddress}
                                    onChange={(e) =>
                                        setUseSameAddress(e.target.checked)
                                    }
                                    className="mr-2"
                                />
                                <span>Same as shipping address</span>
                            </label>

                            {!useSameAddress && (
                                <div className="grid grid-cols-2 gap-4">
                                    {/* Same fields as shipping address */}
                                    {/* Add billing address fields here - same as shipping */}
                                </div>
                            )}
                        </div>
                    </div>

                    {/* Order Summary */}
                    <div className="lg:col-span-1">
                        <div className="bg-card rounded-lg shadow p-6 sticky top-20 border border-border">
                            <h2 className="text-xl font-bold mb-4 text-card-foreground">
                                Order Summary
                            </h2>

                            <div className="space-y-2 mb-4">
                                {items.map((item) => (
                                    <div
                                        key={item.id}
                                        className="flex justify-between text-sm"
                                    >
                                        <span className="text-muted-foreground">
                                            {item.product.name} x{item.quantity}
                                        </span>
                                        <span className="text-foreground">
                                            €
                                            {(
                                                item.product.price *
                                                item.quantity
                                            ).toFixed(2)}
                                        </span>
                                    </div>
                                ))}
                            </div>

                            <div className="border-t border-border pt-4 mb-6">
                                <div className="flex justify-between text-lg font-bold">
                                    <span className="text-foreground">
                                        Total
                                    </span>
                                    <span className="text-primary">
                                        €{totalPrice.toFixed(2)}
                                    </span>
                                </div>
                            </div>

                            <Button
                                type="submit"
                                size="lg"
                                className="w-full"
                                isLoading={isProcessing}
                                disabled={isProcessing}
                            >
                                Place Order
                            </Button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    );
}
