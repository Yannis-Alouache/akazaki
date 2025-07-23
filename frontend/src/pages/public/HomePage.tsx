import { Button } from "../../components/common";

export function HomePage() {
    return (
        <div className="space-y-20">
            {/* Hero Section */}
            <section className="bg-card border border-border shadow-lg rounded-2xl p-12 text-center">
                <h1 className="text-5xl font-extrabold text-foreground mb-4">
                    Le goût du Japon, bouchée après bouchée 🍘
                </h1>
                <p className="text-xl text-muted-foreground mb-8">
                    Découvrez notre sélection de snacks japonais authentiques,
                    livrés directement chez vous.
                </p>
                <div className="flex justify-center gap-4 flex-wrap">
                    <Button size="lg">
                        <a href="/products">Acheter maintenant</a>
                    </Button>
                    <Button variant="secondary" size="lg">
                        <a href="/about">Pourquoi Akazaki&nbsp;?</a>
                    </Button>
                </div>
            </section>

            {/* Featured Benefits */}
            <section className="grid grid-cols-1 md:grid-cols-3 gap-8">
                {[
                    {
                        icon: "🎌",
                        title: "Sélectionné au Japon",
                        desc: "Des friandises authentiques provenant directement des meilleurs artisans japonais.",
                    },
                    {
                        icon: "🚚",
                        title: "Livraison rapide & gratuite",
                        desc: "Profitez de la livraison gratuite dans toute l’UE pour toute commande supérieure à 50€.",
                    },
                    {
                        icon: "🌟",
                        title: "Apprécié par 1 000+ clients",
                        desc: "Note de 4,9/5 avec des centaines d’avis élogieux.",
                    },
                ].map(({ icon, title, desc }, index) => (
                    <div
                        key={index}
                        className="bg-card rounded-xl border border-border shadow p-6 text-center"
                    >
                        <div className="text-5xl mb-4">{icon}</div>
                        <h3 className="text-lg font-bold text-foreground mb-2">
                            {title}
                        </h3>
                        <p className="text-muted-foreground">{desc}</p>
                    </div>
                ))}
            </section>


            {/* Promo CTA */}
            <section className="bg-primary text-primary-foreground rounded-2xl p-10 text-center space-y-4">
                <h2 className="text-3xl font-bold">
                    Obtenez 15% de réduction sur votre première commande
                </h2>
                <p className="text-xl">
                    Utilisez le code <strong>AKAZAKI15</strong> lors de votre commande
                </p>
                <Button size="lg" variant="secondary">
                    <a href="/products">Obtenir la réduction</a>
                </Button>
            </section>
        </div>
    );
}
