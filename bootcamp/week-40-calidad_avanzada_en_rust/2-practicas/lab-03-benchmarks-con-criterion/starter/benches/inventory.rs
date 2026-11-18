// PASO 1
// use criterion::{Criterion, criterion_group, criterion_main};
// use lab03::{catalog, merge_copies};
// use std::hint::black_box;
//
// fn merge(c: &mut Criterion) {
//     let books = catalog(1000);
//     c.bench_function("merge_copies 1000", |b| {
//         b.iter(|| merge_copies(black_box(&books)))
//     });
// }
//
// PASO 2
// use criterion::{BenchmarkId, Throughput};
//
// fn merge_sizes(c: &mut Criterion) {
//     let mut group = c.benchmark_group("merge_copies");
//     for n in [100, 1000, 10_000] {
//         let books = catalog(n);
//         group.throughput(Throughput::Elements(n as u64));
//         group.bench_with_input(BenchmarkId::from_parameter(n), &books, |b, books| {
//             b.iter(|| merge_copies(black_box(books)))
//         });
//     }
//     group.finish();
// }
//
// PASO 1
// criterion_group!(benches, merge);
// criterion_main!(benches);
