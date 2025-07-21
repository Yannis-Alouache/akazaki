import { Button, ComponentErrorBoundary } from "../../components/common";
import { ErrorTester } from "../../components/common/ErrorTester";

export function HomePage() {

  return (
    <div className="space-y-12">
      {/* Hero Section */}
      <section className="text-center py-12 bg-card rounded-2xl shadow-lg border border-border">
        <h1 className="text-5xl font-bold text-foreground mb-4">
          Welcome to Akazaki Snacks! 🍿
        </h1>
        <p className="text-xl text-muted-foreground mb-8">
          Discover our delicious collection of authentic Japanese snacks
        </p>
        <div className="flex gap-4 justify-center">
          <a href="/products">
            <Button size="lg">
              Browse Products
            </Button>
          </a>
          <a href="/about">
            <Button variant="secondary" size="lg">
              Learn More
            </Button>
          </a>
        </div>
      </section>

       <section>
        <h2 className="text-2xl font-bold text-foreground mb-4">Error Boundary Testing</h2>
        <ComponentErrorBoundary>
          <ErrorTester />
        </ComponentErrorBoundary>
      </section>

      {/* Features Section */}
      <section className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-card p-6 rounded-lg shadow border border-border text-center">
          <div className="text-4xl mb-4">🚚</div>
          <h3 className="text-lg font-semibold text-foreground mb-2">Free Shipping</h3>
          <p className="text-muted-foreground">On orders over €50</p>
        </div>
        <div className="bg-card p-6 rounded-lg shadow border border-border text-center">
          <div className="text-4xl mb-4">✨</div>
          <h3 className="text-lg font-semibold text-foreground mb-2">Premium Quality</h3>
          <p className="text-muted-foreground">Authentic Japanese snacks</p>
        </div>
        <div className="bg-card p-6 rounded-lg shadow border border-border text-center">
          <div className="text-4xl mb-4">💖</div>
          <h3 className="text-lg font-semibold text-foreground mb-2">Customer Love</h3>
          <p className="text-muted-foreground">4.9/5 rating from happy customers</p>
        </div>
      </section>

      {/* CTA Section */}
      <section className="bg-primary text-primary-foreground p-8 rounded-2xl text-center">
        <h2 className="text-3xl font-bold mb-4">New Customer Special!</h2>
        <p className="text-xl mb-6">Get 15% off your first order with code: AKAZAKI15</p>
        <a href="/products">
          <button className="bg-card text-card-foreground px-6 py-3 rounded-lg font-semibold hover:opacity-90 transition-opacity">
            Shop Now
          </button>
        </a>
      </section>
    </div>
  );
}