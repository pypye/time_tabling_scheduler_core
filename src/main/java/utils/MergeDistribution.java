package utils;

public class MergeDistribution {
    private final int[] root;

    public MergeDistribution(int n) {
        root = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            root[i] = i;
        }
    }

    public int getRoot(int u) {
        if (u == root[u]) {
            return u;
        }
        return root[u] = getRoot(root[u]);
    }

    public int[] getRootList() {
        return root;
    }

    public void merge(int u, int v) {
        u = getRoot(u);
        v = getRoot(v);
        if (u != v) {
            root[v] = u;
        }
    }
}
