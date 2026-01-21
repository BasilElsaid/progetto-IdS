/**
 * UTENTE (store)
 * - UI per shop/ordini/carrello
 * - usa fetch() per chiamate HTTP
 * - usa localStorage per token e settings
 */

const $ = (id) => document.getElementById(id);

const st = {
    // se la pagina è servita da Spring Boot (localhost:8080), baseUrl vuoto va benissimo
    baseUrl: localStorage.getItem("filiera.baseUrl") || "",
    token: localStorage.getItem("filiera.userToken") || "",
    buyerId: localStorage.getItem("filiera.buyerId") || "",
    activeTab: "home",
    annunci: [],
    cat: "Tutti",
    cart: null,
    lastOrderId: localStorage.getItem("filiera.lastOrderId") || ""
};

// init campi UI (se esistono)
if ($("baseUrlLabel")) $("baseUrlLabel").textContent = (st.baseUrl || "").replace(/\/+$/,"") || "stesso server";
if ($("buyerId")) $("buyerId").value = st.buyerId || "";
if ($("buyerIdSet")) $("buyerIdSet").value = st.buyerId || "";
if ($("baseUrl")) $("baseUrl").value = st.baseUrl;
if ($("tokenBox")) $("tokenBox").value = st.token;
if ($("lastOrderId")) $("lastOrderId").value = st.lastOrderId;

function setAuthPill(){
    if (!$("authPill")) return;
    $("authPill").textContent = st.token ? "Autenticato" : "Non autenticato";
}
setAuthPill();

function money(v){
    const n = Number(v || 0);
    return "€ " + n.toFixed(2);
}

function escapeHtml(s){
    return String(s ?? "")
        .replaceAll("&","&amp;").replaceAll("<","&lt;").replaceAll(">","&gt;")
        .replaceAll('"',"&quot;").replaceAll("'","&#039;");
}

function base(){
    const b = (st.baseUrl || "").trim().replace(/\/+$/,"");
    return b;
}

async function api(path, { method="GET", body=null } = {}){
    // se baseUrl è vuoto, usa path relativo (stesso host/porta)
    const url = (base() ? base() : "") + path;

    const headers = { "Accept":"application/json" };
    if (st.token) headers["Authorization"] = "Bearer " + st.token;
    if (!["GET","HEAD"].includes(method)) headers["Content-Type"] = "application/json";

    const opt = { method, headers };
    if (body && !["GET","HEAD"].includes(method)) opt.body = JSON.stringify(body);

    const r = await fetch(url, opt);
    const text = await r.text();
    let data = text;
    try { data = text ? JSON.parse(text) : null; } catch {}

    if (!r.ok){
        const msg =
            (data && typeof data === "object" && (data.message || data.detail))
                ? (data.message || data.detail)
                : text;
        throw new Error(`HTTP ${r.status}: ${msg || "Errore"}`);
    }
    return data;
}

// immagini hero
const HERO = [
    "https://images.unsplash.com/photo-1542838132-92c53300491e?auto=format&fit=crop&w=1900&q=80",
    "https://images.unsplash.com/photo-1500595046743-cd271d694d30?auto=format&fit=crop&w=1900&q=80",
    "https://images.unsplash.com/photo-1514996937319-344454492b37?auto=format&fit=crop&w=1900&q=80",
    "https://images.unsplash.com/photo-1543363136-1bfa4b5e1b0b?auto=format&fit=crop&w=1900&q=80"
];

let heroIdx = 0;

function setHero(){
    const el = $("heroSlide");
    if(!el) return;

    el.style.setProperty("--img", `url("${HERO[heroIdx]}")`);
    el.style.backgroundImage = `url("${HERO[heroIdx]}")`;

    // dots
    const dots = $("heroDots");
    if (!dots) return;
    dots.innerHTML = "";
    HERO.forEach((_, i)=>{
        const d = document.createElement("div");
        d.className = "dot" + (i===heroIdx ? " active" : "");
        d.onclick = ()=>{ heroIdx=i; setHero(); };
        dots.appendChild(d);
    });
}

function renderHero(){
    const el = $("heroSlide");
    if(!el) return;

    if (!document.getElementById("heroStyle")){
        const s = document.createElement("style");
        s.id = "heroStyle";
        s.textContent = `.heroSlide::before{ background-image: var(--img); }`;
        document.head.appendChild(s);
    }
    setHero();
}

if ($("heroPrev")) $("heroPrev").onclick = ()=>{ heroIdx = (heroIdx - 1 + HERO.length) % HERO.length; renderHero(); };
if ($("heroNext")) $("heroNext").onclick = ()=>{ heroIdx = (heroIdx + 1) % HERO.length; renderHero(); };
renderHero();

function showTab(name){
    st.activeTab = name;
    document.querySelectorAll(".navLink").forEach(a => a.classList.toggle("active", a.dataset.tab === name));
    ["home","shop","about","contact","orders"].forEach(t => {
        const el = $("tab_"+t);
        if (el) el.classList.toggle("hidden", t !== name);
    });
}

const nav = $("nav");
if (nav){
    nav.addEventListener("click", (e)=>{
        const a = e.target.closest(".navLink");
        if(!a) return;
        e.preventDefault();
        showTab(a.dataset.tab);
    });
}

if ($("goShop")) $("goShop").onclick = ()=> showTab("shop");
if ($("goAbout")) $("goAbout").onclick = ()=> showTab("about");

if ($("btnAccount")) $("btnAccount").onclick = ()=> openAccount();
if ($("btnCart")) $("btnCart").onclick = ()=> openCart();

function openAccount(){ if ($("accountModal")) $("accountModal").classList.remove("hidden"); }
function closeAccount(){ if ($("accountModal")) $("accountModal").classList.add("hidden"); }
if ($("btnCloseAccount")) $("btnCloseAccount").onclick = closeAccount;

function openCart(){ if ($("cartDrawer")) $("cartDrawer").classList.remove("hidden"); refreshCart(); }
function closeCart(){ if ($("cartDrawer")) $("cartDrawer").classList.add("hidden"); }
if ($("btnCloseCart")) $("btnCloseCart").onclick = closeCart;

if ($("btnMenu")) $("btnMenu").onclick = ()=> showTab("shop");

document.querySelectorAll(".tabBtn").forEach(b=>{
    b.onclick = ()=>{
        document.querySelectorAll(".tabBtn").forEach(x=>x.classList.remove("active"));
        b.classList.add("active");
        ["login","register","settings"].forEach(k => {
            const el = $("pane_"+k);
            if (el) el.classList.toggle("hidden", b.dataset.tab !== k);
        });
    };
});

function msg(id, text, ok=true){
    const el = $(id);
    if(!el) return;
    el.textContent = text;
    el.style.color = ok ? "#2f6f3e" : "#b91c1c";
}

if ($("btnSaveSettings")){
    $("btnSaveSettings").onclick = ()=>{
        st.baseUrl = ($("baseUrl")?.value || "").trim();
        st.buyerId = ($("buyerIdSet")?.value || "").trim();

        localStorage.setItem("filiera.baseUrl", st.baseUrl);
        localStorage.setItem("filiera.buyerId", st.buyerId);

        if ($("baseUrlLabel")) $("baseUrlLabel").textContent = base() || "stesso server";
        if ($("buyerId")) $("buyerId").value = st.buyerId;
        msg("loginMsg", "Impostazioni salvate.");
    };
}

if ($("btnUseToken")){
    $("btnUseToken").onclick = ()=>{
        st.token = ($("tokenBox")?.value || "").trim();
        localStorage.setItem("filiera.userToken", st.token);
        setAuthPill();
        msg("loginMsg", "Token aggiornato.");
    };
}

if ($("btnClearToken")){
    $("btnClearToken").onclick = ()=>{
        st.token = "";
        localStorage.removeItem("filiera.userToken");
        if ($("tokenBox")) $("tokenBox").value = "";
        setAuthPill();
        msg("loginMsg", "Token rimosso.");
    };
}

// registrazione acquirente: POST /api/acquirenti
if ($("btnRegister")){
    $("btnRegister").onclick = async ()=>{
        const u = ($("regUser")?.value || "").trim();
        const e = ($("regEmail")?.value || "").trim();
        const p = $("regPass")?.value || "";
        const p2 = $("regPass2")?.value || "";

        if(!u || !e || !p) return msg("regMsg","Compila tutti i campi", false);
        if(p !== p2) return msg("regMsg","Le password non coincidono", false);

        try{
            const res = await api("/api/acquirenti", { method:"POST", body:{ username:u, password:p, email:e }});
            const id = res?.id;
            if(id){
                st.buyerId = String(id);
                localStorage.setItem("filiera.buyerId", st.buyerId);
                if ($("buyerId")) $("buyerId").value = st.buyerId;
                if ($("buyerIdSet")) $("buyerIdSet").value = st.buyerId;
            }
            msg("regMsg", `Registrazione OK${id ? " • Il tuo ID = "+id : ""}. Ora fai login.`);
        }catch(err){
            msg("regMsg", err.message, false);
        }
    };
}

// login: ADATTA QUI IL PATH SE IL TUO È /api/auth/login
if ($("btnLogin")){
    $("btnLogin").onclick = async ()=>{
        const u = ($("loginUser")?.value || "").trim();
        const p = $("loginPass")?.value || "";

        if(!u || !p) return msg("loginMsg","Inserisci username e password", false);

        try{
            // Cambia questo path se il tuo swagger usa un altro endpoint
            const res = await api("/auth/login", { method:"POST", body:{ username:u, password:p }});
            const token = res?.token || res?.accessToken || res?.jwt || res?.bearer;
            if(!token) return msg("loginMsg","Login OK ma token non trovato nella risposta JSON", false);

            st.token = token;
            localStorage.setItem("filiera.userToken", token);
            if ($("tokenBox")) $("tokenBox").value = token;
            setAuthPill();

            msg("loginMsg","Login OK. Vai allo shop e aggiungi prodotti al carrello.");
            closeAccount();
            await loadAnnunci();
        }catch(err){
            msg("loginMsg", err.message, false);
        }
    };
}

if ($("btnRefresh")) $("btnRefresh").onclick = ()=> loadAnnunci();
if ($("q")) $("q").oninput = ()=> renderGrid();
if ($("sort")) $("sort").onchange = ()=> renderGrid();

function pickName(a){ return a?.titolo || a?.prodotto?.nome || a?.nome || "Prodotto"; }
function pickCat(a){ return a?.categoria || a?.prodotto?.categoria || "ALTRO"; }
function pickAzienda(a){ return a?.azienda?.nomeAzienda || a?.aziendaNome || a?.azienda?.nome || "Azienda"; }
function pickPrice(a){ return a?.prezzo ?? a?.prodotto?.prezzo ?? 0; }
function pickStock(a){ return a?.stock ?? a?.quantitaDisponibile ?? a?.disponibilita ?? 0; }

function imgFor(a){
    const id = a?.id || a?.prodotto?.id || 1;
    const pics = [
        "https://images.unsplash.com/photo-1542838132-92c53300491e?auto=format&fit=crop&w=900&q=80",
        "https://images.unsplash.com/photo-1514996937319-344454492b37?auto=format&fit=crop&w=900&q=80",
        "https://images.unsplash.com/photo-1543363136-1bfa4b5e1b0b?auto=format&fit=crop&w=900&q=80",
        "https://images.unsplash.com/photo-1506806732259-39c2d0268443?auto=format&fit=crop&w=900&q=80",
        "https://images.unsplash.com/photo-1541592106381-b31e9677c0e5?auto=format&fit=crop&w=900&q=80",
        "https://images.unsplash.com/photo-1514516870926-206b27411f74?auto=format&fit=crop&w=900&q=80"
    ];
    return pics[id % pics.length];
}

// marketplace annunci: GET /api/marketplace/annunci
async function loadAnnunci(){
    try{
        const list = await api("/api/marketplace/annunci", { method:"GET" });
        st.annunci = Array.isArray(list) ? list : [];
        buildCats();
        renderGrid();
    }catch(err){
        if ($("grid")) $("grid").innerHTML = "";
        if ($("empty")){
            $("empty").classList.remove("hidden");
            $("empty").textContent = err.message;
        }
    }
}

function buildCats(){
    const counts = new Map();
    st.annunci.forEach(a=>{
        const c = String(pickCat(a));
        counts.set(c, (counts.get(c)||0)+1);
    });

    const cats = ["Tutti", ...Array.from(counts.keys()).sort()];
    const box = $("catList");
    if(!box) return;
    box.innerHTML = "";

    cats.forEach(c=>{
        const el = document.createElement("div");
        el.className = "cat" + (st.cat===c ? " active" : "");
        const n = c==="Tutti" ? st.annunci.length : (counts.get(c)||0);
        el.innerHTML = `<div class="catName">${escapeHtml(c)}</div><div class="catCount">${n}</div>`;
        el.onclick = ()=>{ st.cat=c; buildCats(); renderGrid(); };
        box.appendChild(el);
    });
}

function renderGrid(){
    const q = (($("q")?.value || "")).trim().toLowerCase();
    const sort = $("sort")?.value || "priceAsc";

    let list = st.annunci.slice();
    if (st.cat !== "Tutti") list = list.filter(a => String(pickCat(a)) === st.cat);
    if (q) list = list.filter(a => (pickName(a)+" "+pickAzienda(a)+" "+pickCat(a)).toLowerCase().includes(q));

    if (sort==="priceAsc") list.sort((a,b)=> pickPrice(a)-pickPrice(b));
    if (sort==="priceDesc") list.sort((a,b)=> pickPrice(b)-pickPrice(a));
    if (sort==="stockDesc") list.sort((a,b)=> pickStock(b)-pickStock(a));

    const grid = $("grid");
    if(!grid) return;
    grid.innerHTML = "";

    if (!list.length){
        if ($("empty")) $("empty").classList.remove("hidden");
        return;
    }
    if ($("empty")) $("empty").classList.add("hidden");

    list.forEach(a=>{
        const card = document.createElement("div");
        card.className = "cardP";
        card.innerHTML = `
      <img src="${imgFor(a)}" alt="foto prodotto" loading="lazy"/>
      <div class="cardBody">
        <div class="cardTitle">${escapeHtml(pickName(a))}</div>
        <div class="cardMeta">
          <span class="tag">${escapeHtml(pickAzienda(a))}</span>
          <span class="tag">${escapeHtml(pickCat(a))}</span>
        </div>
        <div class="priceRow">
          <div>
            <div class="price">${money(pickPrice(a))}</div>
            <div class="stock">Stock: ${pickStock(a)}</div>
          </div>
          <div class="qtyRow">
            <input class="qty" type="number" min="1" value="1" id="qty_${a.id}"/>
            <button class="btn primary small" data-add="${a.id}">+</button>
          </div>
        </div>
      </div>
    `;
        grid.appendChild(card);
    });
}

if ($("grid")){
    $("grid").addEventListener("click", async (e)=>{
        const b = e.target.closest("[data-add]");
        if(!b) return;

        const annuncioId = Number(b.dataset.add);
        const quantita = Math.max(1, Number($("qty_"+annuncioId)?.value || 1));

        const acquirenteId = String(st.buyerId || ($("buyerId")?.value || "").trim());
        if(!acquirenteId) return alert("Inserisci Acquirente ID in Account → Impostazioni o nella sezione Ordini.");

        try{
            await api(`/api/carrello/${acquirenteId}/items`, { method:"POST", body:{ annuncioId, quantita }});
            await refreshCart(true);
            openCart();
        }catch(err){
            alert(err.message);
        }
    });
}

// carrello: GET /api/carrello/{acquirenteId}
async function refreshCart(silent=false){
    const acquirenteId = String(st.buyerId || ($("buyerId")?.value || "").trim());
    if(!acquirenteId){
        if ($("cartSub")) $("cartSub").textContent = "Inserisci Acquirente ID.";
        if ($("cartItems")) $("cartItems").innerHTML = "";
        if ($("cartTotal")) $("cartTotal").textContent = money(0);
        if ($("cartCount")) $("cartCount").textContent = "0";
        return;
    }

    try{
        const cart = await api(`/api/carrello/${acquirenteId}`, { method:"GET" });
        st.cart = cart;

        const items = cart?.items || [];
        if ($("cartCount")) $("cartCount").textContent = String(items.length);
        if ($("cartSub")) $("cartSub").textContent = items.length ? `${items.length} articoli` : "Carrello vuoto";

        if ($("cartItems")) $("cartItems").innerHTML = "";
        let total = 0;

        items.forEach(it=>{
            const a = it.annuncio || it.annuncioMarketplace || it;
            const qty = it.quantita || 1;
            const unit = it.prezzoUnitario ?? pickPrice(a);
            const line = Number(qty) * Number(unit);
            total += line;

            const row = document.createElement("div");
            row.className = "lineItem";
            row.innerHTML = `
        <div class="liLeft">
          <img class="thumb" src="${imgFor(a)}" alt="thumb" />
          <div>
            <div class="liT">${escapeHtml(pickName(a))}</div>
            <div class="liS">Qty ${qty} • ${money(unit)}</div>
          </div>
        </div>
        <button class="btn danger small" data-del="${it.id}">Rimuovi</button>
      `;
            if ($("cartItems")) $("cartItems").appendChild(row);
        });

        if ($("cartTotal")) $("cartTotal").textContent = money(total);
        if(!silent && $("lastOrderId")) $("lastOrderId").value = st.lastOrderId || $("lastOrderId").value;

    }catch(err){
        if(!silent) alert(err.message);
    }
}

if ($("cartItems")){
    $("cartItems").addEventListener("click", async (e)=>{
        const b = e.target.closest("[data-del]");
        if(!b) return;

        const itemId = Number(b.dataset.del);
        const acquirenteId = String(st.buyerId || ($("buyerId")?.value || "").trim());
        try{
            await api(`/api/carrello/${acquirenteId}/items/${itemId}`, { method:"DELETE" });
            await refreshCart(true);
        }catch(err){
            alert(err.message);
        }
    });
}

// checkout: POST /api/carrello/{id}/checkout
if ($("btnCheckout")){
    $("btnCheckout").onclick = async ()=>{
        const acquirenteId = String(st.buyerId || ($("buyerId")?.value || "").trim());
        if(!acquirenteId) return alert("Inserisci Acquirente ID.");

        try{
            const ordine = await api(`/api/carrello/${acquirenteId}/checkout`, { method:"POST", body:{} });
            const id = ordine?.id || ordine?.ordineId;
            if(id){
                st.lastOrderId = String(id);
                localStorage.setItem("filiera.lastOrderId", st.lastOrderId);
                if ($("lastOrderId")) $("lastOrderId").value = st.lastOrderId;
            }
            alert(`Checkout OK. Ordine creato: ${id ?? "—"}`);
            await refreshCart(true);
        }catch(err){
            alert(err.message);
        }
    };
}

// paga: POST /api/carrello/{id}/paga
if ($("btnPay")){
    $("btnPay").onclick = async ()=>{
        const acquirenteId = String(st.buyerId || ($("buyerId")?.value || "").trim());
        const ordineId = Number(($("lastOrderId")?.value || "").trim() || 0);
        const metodo = $("payMethod")?.value || "CARTA";

        if(!acquirenteId) return alert("Inserisci Acquirente ID.");
        if(!ordineId) return alert("Inserisci Ordine ID (dopo checkout).");

        try{
            await api(`/api/carrello/${acquirenteId}/paga`, { method:"POST", body:{ ordineId, metodo }});
            alert("Pagamento OK.");
        }catch(err){
            alert(err.message);
        }
    };
}

// ordini: GET /api/ordini/acquirente/{id}
if ($("btnLoadOrders")){
    $("btnLoadOrders").onclick = async ()=>{
        const id = ($("buyerId")?.value || "").trim();
        if(!id) return alert("Inserisci Acquirente ID.");

        try{
            const list = await api(`/api/ordini/acquirente/${id}`, { method:"GET" });
            renderOrders(Array.isArray(list) ? list : []);
        }catch(err){
            alert(err.message);
        }
    };
}

function badge(stato){
    const s = String(stato||"—").toUpperCase();
    let bg = "#f1f5f9", bd = "#e2e8f0", tx = "#334155";
    if (s.includes("ATTESA")) { bg="#fff7ed"; bd="#fed7aa"; tx="#9a3412"; }
    if (s.includes("PAGAT"))  { bg="#ecfdf5"; bd="#a7f3d0"; tx="#065f46"; }
    if (s.includes("SPED"))   { bg="#eff6ff"; bd="#bfdbfe"; tx="#1d4ed8"; }
    if (s.includes("CONS"))   { bg="#ecfdf5"; bd="#a7f3d0"; tx="#065f46"; }
    if (s.includes("RIMB") || s.includes("ANNULL")) { bg="#fef2f2"; bd="#fecaca"; tx="#991b1b"; }

    return `<span style="display:inline-flex;padding:6px 10px;border-radius:999px;border:1px solid ${bd};background:${bg};color:${tx};font-weight:800;font-size:12px;">${escapeHtml(stato||"—")}</span>`;
}

function renderOrders(list){
    const box = $("ordersList");
    if(!box) return;

    box.innerHTML = "";
    if(!list.length){
        if ($("ordersEmpty")) $("ordersEmpty").classList.remove("hidden");
        return;
    }
    if ($("ordersEmpty")) $("ordersEmpty").classList.add("hidden");

    list.slice().reverse().forEach(o=>{
        const id = o.id || o.ordineId;
        const row = document.createElement("div");
        row.className = "lineItem";
        row.innerHTML = `
      <div class="liLeft">
        <div>
          <div class="liT">Ordine #${escapeHtml(id)}</div>
          <div class="liS">${badge(o.stato)} • Totale ${money(o.importoTotale ?? o.totale ?? 0)}</div>
        </div>
      </div>
      <button class="btn small" data-open="${id}">Dettagli</button>
    `;
        box.appendChild(row);
    });

    box.onclick = async (e)=>{
        const b = e.target.closest("[data-open]");
        if(!b) return;
        const id = b.dataset.open;
        try{
            const ord = await api(`/api/ordini/${id}`, { method:"GET" });
            alert(JSON.stringify(ord, null, 2));
        }catch(err){
            alert(err.message);
        }
    };
}

/** init */
showTab("home");
loadAnnunci().catch(()=>{});
