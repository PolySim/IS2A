interface IGestionAffinage {
  EntityTransaction getTransaction();
  Cave getCaveById(int cave_id) throws CaveInconnuException;
  void createFromage(String type, Date date, int cave_id)
    throws CaveInconnuException;
  Fromage getFromageById(int fromage_id) throws FromageInconnuException;
  Cave getCurrentCave(int fromage_id)
    throws FromageInconnuException, FromageAffineException;
  void moveFromage(int fromage_id, Date new_date, int cave_id)
    throws FromageInconnuException, FromageAffineException, CaveInconnuException;
}
