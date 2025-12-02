import numpy as np
from load_data import load_data
from format_data import group_by_city, normalize_matrix, get_name_by_index
from pageRank import page_rank

def get_top_n(q: np.ndarray, map_key: dict[str, int], n: int = 20) -> list[tuple[str, float]]:
    """Returns the top n pages with their PageRank scores."""
    top_indices = np.argsort(q)[::-1][:n]
    result = []
    for idx in top_indices:
        name = get_name_by_index(map_key, idx)
        result.append((name, q[idx]))
    return result

def analyze_standard_pagerank(matrix_norm: np.ndarray, map_key: dict[str, int], beta: float = 0.85):
    print(f"\n--- Standard PageRank (beta={beta}) ---")
    (q, count) = page_rank(matrix_norm, beta, 1e-8)
    print(f"Converged in {count} iterations.")
    
    top_20 = get_top_n(q, map_key, 20)
    print("Top 20:")
    for i, (name, score) in enumerate(top_20):
        print(f"{i+1}. {name}: {score:.6f}")
    return top_20

def analyze_personalized_pagerank(matrix_norm: np.ndarray, map_key: dict[str, int], themes: list[str], beta: float = 0.85):
    n = matrix_norm.shape[0]
    
    for theme in themes:
        print(f"\n--- Personalized PageRank (Theme: {theme}, beta={beta}) ---")
        if theme not in map_key:
            print(f"Warning: Theme '{theme}' not found in data.")
            continue
            
        theme_idx = map_key[theme]
        v = np.zeros(n)
        v[theme_idx] = 1.0
        
        (q, count) = page_rank(matrix_norm, beta, 1e-8, v=v)
        print(f"Converged in {count} iterations.")
        
        top_20 = get_top_n(q, map_key, 20)
        print("Top 20:")
        for i, (name, score) in enumerate(top_20):
            print(f"{i+1}. {name}: {score:.6f}")

def main():
    print("Loading data...")
    data = load_data()
    print("Grouping by city (creating matrix)...")
    (matrix_initial, map_key) = group_by_city(data)
    print("Normalizing matrix...")
    matrix_norm = normalize_matrix(matrix_initial)
    
    # Standard PageRank
    analyze_standard_pagerank(matrix_norm, map_key, beta=0.85)
    
    # Personalized PageRank
    # Themes chosen based on likely popular nodes or distinct categories
    themes = ["United_States", "Science", "Earth"] 
    analyze_personalized_pagerank(matrix_norm, map_key, themes, beta=0.85)

if __name__ == "__main__":
    main()
