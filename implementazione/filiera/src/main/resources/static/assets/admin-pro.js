/**
 * Admin Pro
 * - Console admin con lista moduli + CRUD veloce
 * - fetch() per chiamate HTTP
 */

const $ = (id) => document.getElementById(id);

const LOGIN_PATH = "/auth/login"; // cambia in "/api/auth/login" se serve
const SWAGGER_PATH = "/swagger-ui/index.html";

const st = {
    baseUrl: localStorage.getItem("filiera.baseUrl") || "",
    adminToken: localStorage.getItem("filiera.admin.token") || "",
    buyerId: localStorage.getItem("filiera.buyerId") || "1",
    activeModule: null,
    lastList: []
};

function base() { return (st.baseUrl || "").trim().replace(/\/+$/, ""); }

function urlJoin(path){
    return (base() ? base() : "") + path;
}

function pretty(v){ try { return JSON.stringify(v, null, 2); } catch { return String(v); } }

function setText(id, text){
    const el = $(id);
    if (el) el.textContent = text;
}

function decodeJwtRole(token){
    try{
        const parts = token.split(".");
        if (parts.length < 2) return "—";
        const payload = parts[1].replace(/-/g, "+").replace(/_/g, "/");
        const json = JSON.parse(atob(payload));
        return json.ruolo || json.role ||
            (Array.isArray(json.roles) ? json.roles.join(",") : json.roles) ||
            (Array.isArray(json.authorities) ? json.authorities.join(",") : json.authorities) ||
            "—";
    }catch{
        return "—";
    }
}

async function api(path, { method="GET", body=null } = {}){
    const url = urlJoin(path);
    const headers = { "Accept": "application/json" };
    if (st.adminToken) headers["Authorization"] = "Bearer " + st.adminToken;
    if (!["GET","HEAD"].includes(method)) headers["Content-Type"] = "application/json";

    const opt = { method, headers };
    if (body && !["GET","HEAD"].includes(method)) opt.body = JSON.stringify(body);

    const t0 = performance.now();
    const r = await fetch(url, opt);
    const ms = Math.round(performance.now() - t0);

    const text = await r.text();
    let data = text;
    try { data = text ? JSON.parse(text) : null; } catch {}

    if(!r.ok){
        const msg = (data && typeof data === "object" && (data.message || data.detail))
            ? (data.message || data.detail)
            : (text || "Errore");
        const err = new Error(`HTTP ${r.status}: ${msg}`);
        err.status = r.status;
        err.data = data;
        err.ms = ms;
        throw err;
    }
    return { data, status: r.status, ms };
}

function setAuthUI(){
    const ok = !!st.adminToken;
    const dot = $("dot");
    if (dot) dot.classList.toggle("ok", ok);
    setText("authTop", ok ? "Autenticato" : "Non autenticato");
    setText("authSub", "Ruolo: " + (ok ? decodeJwtRole(st.adminToken) : "—"));
}

function setResp(meta, errText, data){
    setText("respMeta", meta || "—");
    setText("err", errText || "");
    setText("pre", data ? pretty(data) : "{}");
}

function replaceBuyerId(path){
    const id = String(($("buyerIdAdmin")?.value || st.buyerId || "1").trim() || "1");
    return path.replaceAll("{buyerId}", id);
}

function setOp(method, path, bodyObj){
    if ($("m")) $("m").value = method;
    if ($("p")) $("p").value = path;
    if ($("b")) $("b").value = bodyObj != null ? JSON.stringify(bodyObj, null, 2) : "";
    setText("opMeta", `${method} ${path}`);
}

function esc(s){
    return String(s ?? "")
        .replaceAll("&","&amp;").replaceAll("<","&lt;").replaceAll(">","&gt;")
        .replaceAll('"',"&quot;").replaceAll("'","&#039;");
}

/* ---- MODULI: adatta solo se i tuoi path swagger differiscono ---- */
const modules = [
    {
        id: "aziende",
        title: "Aziende",
        sub: "Crea/aggiorna aziende agricole",
        list: { method:"GET", path:"/api/aziende" },
        create: { method:"POST", path:"/api/aziende", template:{ nomeAzienda:"Tenuta Vallechiara", sede:"Comune X", partitaIva:"IT00000000000" } },
        getOne: (id)=>({ method:"GET", path:`/api/aziende/${id}` }),
        update: (id)=>({ method:"PATCH", path:`/api/aziende/${id}`, template:{ sede:"Nuova sede" } }),
        del: (id)=>({ method:"DELETE", path:`/api/aziende/${id}` }),
        titleOf: (x)=> x.nomeAzienda || x.nome || `Azienda #${x.id}`,
        subOf: (x)=> `id=${x.id} • sede=${x.sede ?? "—"} • piva=${x.partitaIva ?? "—"}`
    },
    {
        id: "prodotti",
        title: "Prodotti",
        sub: "Catalogo prodotti",
        list: { method:"GET", path:"/api/prodotti" },
        create: { method:"POST", path:"/api/prodotti", template:{ nome:"Olio EVO", categoria:"OLIO", prezzo:12.0 } },
        getOne: (id)=>({ method:"GET", path:`/api/prodotti/${id}` }),
        update: (id)=>({ method:"PUT", path:`/api/prodotti/${id}`, template:{ nome:"Olio EVO (Nuova annata)", prezzo:13.0 } }),
        del: (id)=>({ method:"DELETE", path:`/api/prodotti/${id}` }),
        titleOf: (x)=> x.nome || `Prodotto #${x.id}`,
        subOf: (x)=> `id=${x.id} • cat=${x.categoria ?? "—"} • prezzo=${x.prezzo ?? "—"}`
    },
    {
        id: "annunci",
        title: "Marketplace",
        sub: "Annunci (prezzo/stock/attivo)",
        list: { method:"GET", path:"/api/marketplace/annunci" },
        create: { method:"POST", path:"/api/marketplace/annunci", template:{ prodottoId:1, aziendaId:1, prezzo:9.9, stock:50, attivo:true } },
        getOne: (id)=>({ method:"GET", path:`/api/marketplace/annunci/${id}` }),
        update: (id)=>({ method:"PATCH", path:`/api/marketplace/annunci/${id}`, template:{ stock:40, attivo:true } }),
        del: (id)=>({ method:"DELETE", path:`/api/marketplace/annunci/${id}` }),
        titleOf: (x)=> x.titolo || x.prodotto?.nome || `Annuncio #${x.id}`,
        subOf: (x)=> `id=${x.id} • prezzo=${x.prezzo ?? "—"} • stock=${x.stock ?? "—"} • attivo=${x.attivo ?? "—"}`
    },
    {
        id: "ordini",
        title: "Ordini",
        sub: "Gestione ordini",
        list: { method:"GET", path:"/api/ordini" },
        getOne: (id)=>({ method:"GET", path:`/api/ordini/${id}` }),
        update: null,
        del: null,
        titleOf: (x)=> `Ordine #${x.id ?? x.ordineId ?? "—"}`,
        subOf: (x)=> `stato=${x.stato ?? "—"} • totale=${x.importoTotale ?? x.totale ?? "—"}`
    },
    {
        id: "carrello",
        title: "Carrello",
        sub: "Test carrello di un acquirente",
        list: { method:"GET", path:"/api/carrello/{buyerId}" },
        quick: [
            { label:"GET carrello", op:{ method:"GET", path:"/api/carrello/{buyerId}" } },
            { label:"POST add item", op:{ method:"POST", path:"/api/carrello/{buyerId}/items", template:{ annuncioId:1, quantita:1 } } },
            { label:"POST checkout", op:{ method:"POST", path:"/api/carrello/{buyerId}/checkout", template:{} } },
            { label:"POST paga", op:{ method:"POST", path:"/api/carrello/{buyerId}/paga", template:{ ordineId:1, metodo:"CARTA" } } }
        ],
        titleOf: (x)=> `Carrello`,
        subOf: (x)=> `items=${(x.items || x.righe || []).length}`
    }
];

function mountNav(){
    const nav = $("nav");
    if (!nav) return;
    nav.innerHTML = "";
    modules.forEach(m=>{
        const el = document.createElement("div");
        el.className = "navItem";
        el.dataset.id = m.id;
        el.innerHTML = `<div class="navTxt">${esc(m.title)}</div><div class="navSub">${esc(m.sub)}</div>`;
        el.onclick = ()=> activate(m.id);
        nav.appendChild(el);
    });
}

function setActiveNav(id){
    document.querySelectorAll(".navItem").forEach(x => x.classList.toggle("active", x.dataset.id === id));
}

function mountQuick(m){
    const box = $("quick");
    if (!box) return;
    box.innerHTML = "";
    const items = m?.quick || [];
    if (!items.length){
        box.innerHTML = `<div class="msg">—</div>`;
        return;
    }
    items.forEach((q)=>{
        const b = document.createElement("button");
        b.className = "btn small";
        b.textContent = q.label;
        b.onclick = ()=>{
            const op = q.op;
            const path = replaceBuyerId(op.path);
            setOp(op.method, path, op.template ?? null);
        };
        box.appendChild(b);
    });
}

async function activate(id){
    const m = modules.find(x=>x.id===id);
    if(!m) return;
    st.activeModule = m;

    setActiveNav(id);
    setText("h1", m.title);
    setText("h2", m.sub);
    setText("listTitle", "Elenco • " + m.title);
    setText("listSub", `${m.list.method} ${m.list.path}`);

    mountQuick(m);

    const listPath = replaceBuyerId(m.list.path);
    setOp(m.list.method, listPath, null);

    await refreshList();
}

function filterList(list){
    const q = (($("q")?.value || "")).trim().toLowerCase();
    if(!q) return list;
    return list.filter(x => pretty(x).toLowerCase().includes(q));
}

function renderList(list){
    const box = $("list");
    if(!box) return;
    box.innerHTML = "";

    const m = st.activeModule;
    const filtered = filterList(list);

    setText("listSub", filtered.length ? `Risultati: ${filtered.length}` : "Nessun risultato.");

    filtered.slice(0,150).forEach(item=>{
        const id = item.id ?? item.ordineId ?? item.numero ?? item.code;
        const title = m?.titleOf ? m.titleOf(item) : `#${id}`;
        const sub = m?.subOf ? m.subOf(item) : "";

        const row = document.createElement("div");
        row.className = "rowItem";
        row.innerHTML = `
      <div>
        <div class="rowTitle">${esc(title)}</div>
        <div class="rowSub">${esc(sub)}</div>
      </div>
      <div class="rowBtns">
        ${m?.getOne && id != null ? `<button class="btn small" data-act="get" data-id="${id}">Apri</button>` : ""}
        ${m?.update && id != null ? `<button class="btn small" data-act="edit" data-id="${id}">Modifica</button>` : ""}
        ${m?.del && id != null ? `<button class="btn small danger" data-act="del" data-id="${id}">Elimina</button>` : ""}
      </div>
    `;
        row.onclick = (e)=>{
            const b = e.target.closest("button");
            if(!b) return;
            onRowAction(b.dataset.act, b.dataset.id);
        };
        box.appendChild(row);
    });
}

function onRowAction(act, id){
    const m = st.activeModule;
    if(!m) return;

    if (act==="get" && m.getOne){
        const op = m.getOne(id);
        setOp(op.method, replaceBuyerId(op.path), null);
        return;
    }
    if (act==="edit" && m.update){
        const op = m.update(id);
        setOp(op.method, replaceBuyerId(op.path), op.template ?? {});
        return;
    }
    if (act==="del" && m.del){
        const op = m.del(id);
        setOp(op.method, replaceBuyerId(op.path), null);
        if (confirm(`Eliminare id ${id}?`)) sendCurrent(true);
    }
}

async function refreshList(){
    const m = st.activeModule;
    if(!m) return;

    if(!st.adminToken){
        setResp("Admin non autenticato", "Fai login admin.", {});
        renderList([]);
        return;
    }

    const listPath = replaceBuyerId(m.list.path);
    try{
        const res = await api(listPath, { method: m.list.method });
        const arr = Array.isArray(res.data) ? res.data : (res.data ? [res.data] : []);
        st.lastList = arr;
        renderList(arr);
        setResp(`OK • ${m.list.method} ${listPath} • ${res.ms}ms`, "", res.data);
    }catch(err){
        st.lastList = [];
        renderList([]);
        setResp(`ERRORE • ${m.list.method} ${listPath}`, err.message, err.data || {});
    }
}

async function sendCurrent(refreshAfter){
    if(!st.adminToken){
        setResp("Admin non autenticato", "Fai login admin.", {});
        return;
    }

    const method = ($("m")?.value || "GET").trim();
    const path = replaceBuyerId(($("p")?.value || "").trim());
    const bodyTxt = ($("b")?.value || "").trim();

    let body = null;
    if (!["GET","HEAD"].includes(method)){
        if (bodyTxt){
            try { body = JSON.parse(bodyTxt); }
            catch(e){
                setResp("Body JSON non valido", String(e), {});
                return;
            }
        } else {
            body = {};
        }
    }

    try{
        const res = await api(path, { method, body });
        setResp(`HTTP ${res.status} • ${res.ms}ms`, "", res.data);
        if (refreshAfter || ["POST","PUT","PATCH","DELETE"].includes(method)) await refreshList();
    }catch(err){
        setResp(`ERRORE ${err.status || ""} • ${err.ms || ""}ms`, err.message, err.data || {});
    }
}

/* ---- WIRE UI ---- */
function init(){
    if ($("baseUrl")) $("baseUrl").value = st.baseUrl;
    if ($("buyerIdAdmin")) $("buyerIdAdmin").value = st.buyerId;

    setAuthUI();
    mountNav();

    $("btnSave")?.addEventListener("click", ()=>{
        st.baseUrl = ($("baseUrl")?.value || "").trim();
        st.buyerId = ($("buyerIdAdmin")?.value || "1").trim() || "1";
        localStorage.setItem("filiera.baseUrl", st.baseUrl);
        localStorage.setItem("filiera.buyerId", st.buyerId);
        setResp("Impostazioni salvate", "", { baseUrl: st.baseUrl, buyerId: st.buyerId });
    });

    $("btnSwagger")?.addEventListener("click", ()=> window.open(urlJoin(SWAGGER_PATH), "_blank"));

    $("btnAdminLogout")?.addEventListener("click", ()=>{
        st.adminToken = "";
        localStorage.removeItem("filiera.admin.token");
        setAuthUI();
        setResp("Logout", "", {});
        renderList([]);
    });

    $("btnAdminLogin")?.addEventListener("click", async ()=>{
        const u = ($("au")?.value || "").trim();
        const p = ($("ap")?.value || "");
        if(!u || !p){
            setText("aMsg", "Inserisci username e password.");
            return;
        }
        setText("aMsg", "Login...");
        try{
            const res = await api(LOGIN_PATH, { method:"POST", body:{ username:u, password:p }});
            const token = res.data?.token || res.data?.accessToken || res.data?.jwt || res.data?.bearer;
            if(!token){
                setText("aMsg", "Login OK ma token non trovato nella risposta.");
                return;
            }
            st.adminToken = token;
            localStorage.setItem("filiera.admin.token", token);
            setText("aMsg", "Login OK ✅");
            setAuthUI();
            await refreshList();
        }catch(err){
            setText("aMsg", err.message);
        }
    });

    $("btnRefresh")?.addEventListener("click", refreshList);
    $("btnCreate")?.addEventListener("click", ()=>{
        const m = st.activeModule;
        if(!m?.create){
            setResp("Nessun create per questo modulo", "", {});
            return;
        }
        setOp(m.create.method, replaceBuyerId(m.create.path), m.create.template ?? {});
    });

    $("btnTemplate")?.addEventListener("click", ()=>{
        const m = st.activeModule;
        if(m?.create?.template){
            setOp(m.create.method, replaceBuyerId(m.create.path), m.create.template);
            return;
        }
        setResp("Nessun template", "", {});
    });

    $("btnClear")?.addEventListener("click", ()=>{
        if ($("b")) $("b").value = "{}";
        setResp("Pulito", "", {});
    });

    $("btnSend")?.addEventListener("click", ()=> sendCurrent(false));
    $("q")?.addEventListener("input", ()=> renderList(st.lastList));

    // modulo di default
    activate("aziende");
}

init();
