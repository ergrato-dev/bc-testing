use smallvec::SmallVec;

fn main() {
    let mut v: SmallVec<[u8; 4]> = SmallVec::new();
    v.extend([1, 2, 3]);
    println!("{v:?}");
}
