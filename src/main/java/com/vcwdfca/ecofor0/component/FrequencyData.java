package com.vcwdfca.ecofor0.component;

import net.minecraft.nbt.*;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.util.math.BlockPos;
import java.util.*;

/** World-global definitions and exclusive manager claims. Unloading is NOT a release. */
public final class FrequencyData extends WorldSavedData {
    public static final String KEY = "superpatternassembly_frequencies";
    private final Map<UUID, Entry> entries = new LinkedHashMap<>();
    public FrequencyData() { super(KEY); }
    public FrequencyData(String name) { super(name); }
    public static FrequencyData get(World world) {
        net.minecraft.world.storage.MapStorage storage = world.getMinecraftServer().getWorld(0).getMapStorage();
        FrequencyData data = (FrequencyData) storage.getOrLoadData(FrequencyData.class, KEY);
        if (data == null) { data = new FrequencyData(); storage.setData(KEY, data); }
        return data;
    }
    public static final class Manager {
        public final UUID identity;
        public final int dimension;
        public final BlockPos pos;
        public Manager(UUID identity, int dimension, BlockPos pos) { this.identity = identity; this.dimension = dimension; this.pos = pos.toImmutable(); }
        @Override public boolean equals(Object value) {
            if (!(value instanceof Manager)) return false;
            Manager other = (Manager) value;
            return identity.equals(other.identity) && dimension == other.dimension && pos.equals(other.pos);
        }
        @Override public int hashCode() { return Objects.hash(identity, dimension, pos); }
        NBTTagCompound save() {
            NBTTagCompound tag = new NBTTagCompound(); tag.setUniqueId("id", identity); tag.setInteger("dimension", dimension); tag.setLong("pos", pos.toLong()); return tag;
        }
        static Manager load(NBTTagCompound tag) {
            return tag.hasUniqueId("id") ? new Manager(tag.getUniqueId("id"), tag.getInteger("dimension"), BlockPos.fromLong(tag.getLong("pos"))) : null;
        }
    }
    public static final class Entry {
        public final UUID id, creator;
        public String name;
        public Manager manager;
        Entry(UUID id, UUID creator, String name) { this.id = id; this.creator = creator; this.name = name; }
    }
    public synchronized Entry find(UUID id) { return entries.get(id); }
    public synchronized List<Entry> list() {
        List<Entry> list = new ArrayList<>(entries.values()); list.sort(Comparator.comparing(e -> e.name)); return list;
    }
    private String validName(String name, UUID except) {
        if (name == null) return null;
        String clean = name.trim();
        if (clean.isEmpty() || clean.length() > 48 || clean.chars().anyMatch(c -> Character.isISOControl(c) || c == 167)) return null;
        for (Entry e : entries.values()) if (!e.id.equals(except) && e.name.equalsIgnoreCase(clean)) return null;
        return clean;
    }
    public synchronized UUID create(UUID creator, String name) {
        String clean = validName(name, null); if (clean == null || entries.size() >= 4096) return null;
        UUID id = UUID.randomUUID(); entries.put(id, new Entry(id, creator, clean)); markDirty(); return id;
    }
    public synchronized boolean rename(UUID id, String name) {
        Entry e = find(id); String clean = validName(name, id); if (e == null || clean == null) return false;
        e.name = clean; markDirty(); return true;
    }
    public synchronized boolean delete(UUID id) { if (entries.remove(id) == null) return false; markDirty(); return true; }
    public synchronized UUID selected(Manager manager) {
        for (Entry e : entries.values()) if (manager.equals(e.manager)) return e.id;
        return null;
    }
    public synchronized boolean select(Manager manager, UUID id) {
        Entry target = id == null ? null : find(id);
        if (id != null && (target == null || target.manager != null && !manager.equals(target.manager))) return false;
        release(manager);
        if (target != null) target.manager = manager;
        markDirty(); return true;
    }
    public synchronized void release(Manager manager) {
        for (Entry e : entries.values()) if (manager.equals(e.manager)) { e.manager = null; markDirty(); }
    }
    @Override public synchronized void readFromNBT(NBTTagCompound tag) {
        entries.clear(); Set<Manager> claimed = new HashSet<>(); NBTTagList list = tag.getTagList("frequencies", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound t = list.getCompoundTagAt(i);
            if (!t.hasUniqueId("id") || !t.hasUniqueId("creator")) continue;
            Entry e = new Entry(t.getUniqueId("id"), t.getUniqueId("creator"), t.getString("name"));
            Manager manager = Manager.load(t.getCompoundTag("manager"));
            if (manager != null && claimed.add(manager)) e.manager = manager;
            entries.putIfAbsent(e.id, e);
        }
    }
    @Override public synchronized NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagList list = new NBTTagList();
        for (Entry e : entries.values()) {
            NBTTagCompound t = new NBTTagCompound(); t.setUniqueId("id", e.id); t.setUniqueId("creator", e.creator); t.setString("name", e.name);
            if (e.manager != null) t.setTag("manager", e.manager.save()); list.appendTag(t);
        }
        tag.setTag("frequencies", list); return tag;
    }
}
